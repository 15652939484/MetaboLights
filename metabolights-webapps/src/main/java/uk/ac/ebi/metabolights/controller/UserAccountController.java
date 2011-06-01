package uk.ac.ebi.metabolights.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.ebi.metabolights.authenticate.IsaTabAuthenticationProvider;
import uk.ac.ebi.metabolights.model.MetabolightsUser;
import uk.ac.ebi.metabolights.properties.PropertyLookup;
import uk.ac.ebi.metabolights.service.EmailService;
import uk.ac.ebi.metabolights.service.TextUtils;
import uk.ac.ebi.metabolights.service.UserService;
import uk.ac.ebi.metabolights.validate.ValidatorMetabolightsUser;

/**
 * Controller for user account handling (new account, update account).<br>
 * New accounts need to go through an approval process:
 * <UL>
 * <LI>User fills in a 'create account' form, this gets validated for all required fields</LI>
 * <LI>If valid, an account with status NEW is created</LI>
 * <LI>The user is sent an email to confirm the creation linked to the provided email address by clicking a unique URL</LI>
 * <LI>When confirmed, the account gets status USER_VERIFIED</LI>
 * <LI>MTBL admin is notified by an email to the admin list which includes a private link to make the user ACTIVE</LI>
 * <LI>When the private link is clicked, the user becomes ACTIVE and is notified that his account is now usable</LI>
 * </UL>
 * @author markr
 *
 */
@Controller
public class UserAccountController extends AbstractController{

	private static Logger logger = Logger.getLogger(UserAccountController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	/* Ensures the ValidatorMetabolightsUser is invoked to validate the input (@Valid further down) */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(new ValidatorMetabolightsUser());
	}

	/**
	 * Forward to the account creation page, with a blank MetabolightsUser. 
	 */
	@RequestMapping(value = "/newAccount")
   	public ModelAndView newAccount() {
    	ModelAndView mav = new ModelAndView("createAccount");
    	mav.addObject(new MetabolightsUser());
    	return mav;
    }
	
	private @Value("#{appProperties.urlConfirmNewAccount}") String confirmNewAccountUrl;

	
    /**
     * Handles creation of a new user account.<br>
     * Verify form input, check for clashes in database, store the
     * new account (status 'NEW') and let the user verify the request.
     *  
     * TODO Ajax username lookup.
     *  
     * @param MetabolightsUser user details
     * @param result Binding result
     * @param model 
     * @return where to navigate next 
     */
	@RequestMapping(value = "/createNewAccount", method = RequestMethod.POST)
    public ModelAndView createNewAccount(@Valid MetabolightsUser metabolightsUser, BindingResult result, Model model) {

    	boolean duplicateUser=false;
    	boolean duplicateEmailAddress=false;

    	if (TextUtils.textHasContent(metabolightsUser.getUserName()) && result.getFieldError("userName")==null && 
    			userNameExists (metabolightsUser.getUserName())) {
    		duplicateUser=true;
    	}
    	if (TextUtils.textHasContent(metabolightsUser.getEmail()) && result.getFieldError("email")==null &&
    			emailExists (metabolightsUser.getEmail())) {
    		duplicateEmailAddress=true;
    	}

    	if (result.hasErrors()|| duplicateUser|| duplicateEmailAddress) {
        	ModelAndView mav = new ModelAndView("createAccount");
        	mav.addObject(metabolightsUser);
        	if (duplicateUser) {
        		// .. It should be possible to set the message through the object error but I didn't manage
        		//ObjectError duplicateUserError = new ObjectError("metabolightsUser", new String() {".."});
        		mav.addObject("dupUserMessage", PropertyLookup.getMessage("Duplicate.metabolightsUser.userName"));
        	}
        	if (duplicateEmailAddress) {
        		mav.addObject("duplicateEmailAddress", PropertyLookup.getMessage("Duplicate.metabolightsUser.email"));
        	}
        	return mav;
        }


    	//TODO try catch
    	//Store the user information in the database, status NEW means still inactive (to be authorized).
    	metabolightsUser.setStatus(MetabolightsUser.userStatus.NEW.toString()); // make account non usable yet
    	metabolightsUser.setUserName(metabolightsUser.getUserName().toLowerCase());
    	metabolightsUser.setDbPassword(IsaTabAuthenticationProvider.encode(metabolightsUser.getDbPassword()));
    	Long newUserId = userService.insert(metabolightsUser);
    	
    	//TODO try catch + delete
    	String uniqueURLParameter=numericSequence(metabolightsUser.getDbPassword());
    	String confirmationURL=confirmNewAccountUrl+"?usr="+metabolightsUser.getUserName()+"&key="+uniqueURLParameter;
    	emailService.sendConfirmNewAccountRequest(metabolightsUser.getEmail(),confirmationURL);

    	
    	//Let user know what will happen next
    	return new ModelAndView("redirect:accountRequested="+metabolightsUser.getUserName());

	}

    /**
     * Look userName up in database. 
     * @param newUserName
     * @return true if userName exists
     */
    private boolean userNameExists (String newUserName) {
    	MetabolightsUser mtblUser = userService.lookupByUserName(newUserName);
		if (mtblUser != null) {
			return true;
		}
    	return false;
    }

    /**
     * Look email up in database. 
     * @param newUserName
     * @return true if userName exists
     */
    private boolean emailExists (String newEmail) {
    	MetabolightsUser mtblUser = userService.lookupByEmail(newEmail);
		if (mtblUser != null) {
			return true;
		}
    	return false;
    }

	/**
	 * Converts a String to a numeric hash, to be used to create a safe confirmation URL without naughty characters.
	 * @param str input String
	 * @return a string with a lot of numbers, like "11348507875108577367104781199057111886965741201228210010966546961"
	 */
	private String numericSequence(String str) {
		StringBuffer numericHash=new StringBuffer();
		for (int i=0; i<str.length();i++) {
			int nmbr=(int)str.charAt(i);
			numericHash.append(nmbr);
		}
		return numericHash.toString();
	}

	
	/**
	 * After user has filled in a new account creation form.
	 * @param userName
	 * @param map
	 * @return
	 */
	@RequestMapping(value={"/accountRequested={userName}"})
	public ModelAndView accountRequested(@PathVariable("userName") String userName, Map<String, Object> map) {
    	ModelAndView mav = new ModelAndView("accountRequested");
    	mav.addObject("userName", userName);
    	return mav;
	}

	
	private @Value("#{appProperties.urlActivateNewAccount}") String activateNewAccountUrl;

	/**
	 * User has verified new account request by clicking a link sent by email. Update the status
	 * of the database row accordingly. Notify admin to activate thie user and provide them a link to do so.
	 * 
	 * @param userName
	 * @param key
	 */
	@RequestMapping(value={"/confirmAccountRequest"}, method = RequestMethod.GET)
	public ModelAndView confirmAccountRequested(@RequestParam("usr") String userName, @RequestParam("key") String key) {
		ModelAndView mav = new ModelAndView("index");
		MetabolightsUser user = userService.lookupByUserName(userName);
		if (user!=null && user.getStatus().equals(MetabolightsUser.userStatus.NEW.toString()) 
				&& numericSequence(user.getDbPassword()).equals(key)   ) {
			//Set user status to Verified
			user.setStatus(MetabolightsUser.userStatus.VERIFIED.toString());
			userService.update(user);
			mav.addObject("message", PropertyLookup.getMessage("msg.verifiedAccount")+" "+userName+".");

			//Notify mtbl admin
	    	String uniqueURLParameter=numericSequence(user.getDbPassword());
	    	String activationURL=activateNewAccountUrl+"?usrId="+user.getUserId() +"&key="+uniqueURLParameter;
			emailService.sendNewAccountAlert(user,activationURL);
		}
		return mav;
	}

	/**
	 * MTBL admin has verified the user can become active. Update the user status to Active, and
	 * let the user know by email.
	 * @param usrId
	 * @param key
	 */
	@RequestMapping(value={"/activateAccount__NotifyUser"}, method = RequestMethod.GET)
	public ModelAndView activateAccount(@RequestParam("usrId") long usrId, @RequestParam("key") String key) {
		ModelAndView mav = new ModelAndView("index");
		MetabolightsUser user = userService.lookupById(usrId);
		if (user!=null && user.getStatus().equals(MetabolightsUser.userStatus.VERIFIED.toString()) 
				&& numericSequence(user.getDbPassword()).equals(key)   ) {
			user.setStatus(MetabolightsUser.userStatus.ACTIVE.toString());
			userService.update(user);
			mav.addObject("message", PropertyLookup.getMessage("msg.activatedAccount")+" "+user.getUserName()+".");
			emailService.sendAccountHasbeenActivated(user);
		}
		return mav;
	}

    /**
     * Look up current user details by using the Id stored in the principal. 
     * @return Spring model and view
     */
	@RequestMapping(value = "/myAccount")
   	public ModelAndView updateAccount() {
		
		ModelAndView mav = null;

		MetabolightsUser principal = ((MetabolightsUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
		if (principal==null) {
        	mav = new ModelAndView("index"); // probably logged out
     	}

		MetabolightsUser metabolightsUser = userService.lookupById(principal.getUserId());
 		mav = new ModelAndView("updateAccount");
 		metabolightsUser.setUserVerifyDbPassword(metabolightsUser.getDbPassword());
    	mav.addObject(metabolightsUser);
    	logger.info("showing account details for "+metabolightsUser.getUserName()+" ID="+metabolightsUser.getUserId());
    	return mav;
    }

	/**
	 * Updates the user details in the database. 
	 * @param metabolightsUser
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
    public ModelAndView updateAccount(@Valid MetabolightsUser metabolightsUser, BindingResult result, Model model) {

    	if (result.hasErrors()) {
        	ModelAndView mav = new ModelAndView("updateAccount");
        	mav.addObject(metabolightsUser);
       		return mav;
        }

    	//Did the user enter a new password? If so, encode it..
    	MetabolightsUser userCheck = userService.lookupByUserName(metabolightsUser.getUserName());
    	if (!userCheck.getDbPassword().equals(metabolightsUser.getDbPassword())) {
    		logger.info("pasword was changed ... was "+userCheck.getDbPassword()+" is now "+metabolightsUser.getDbPassword());
        	metabolightsUser.setDbPassword(IsaTabAuthenticationProvider.encode(metabolightsUser.getDbPassword()));
    	}
    	
    	//Update the user information in the database
    	userService.update(metabolightsUser);
    	//Update the current security context with new attributes
    	try {
			BeanUtils.copyProperties((MetabolightsUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal()), metabolightsUser);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
    	
		//Back to home page, tell user
    	return new ModelAndView("redirect:update-success");
	}

	/**
	 * After succesful update.
	 */
	@RequestMapping({"/update-success"})
	public ModelAndView updateDone() {
	    return new ModelAndView("index", "message", PropertyLookup.getMessage("msg.updatedAccount"));
    }

	
}
