package uk.ac.ebi.metabolights.controller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ebi.bioinvindex.model.VisibilityStatus;
import uk.ac.ebi.metabolights.model.MetabolightsUser;
import uk.ac.ebi.metabolights.model.queue.SubmissionItem;
import uk.ac.ebi.metabolights.model.queue.SubmissionQueue;
import uk.ac.ebi.metabolights.properties.PropertyLookup;
import uk.ac.ebi.metabolights.service.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Controls multi part file upload as described in Reference Documentation 3.0,
 * chapter "15.8 Spring's multipart (fileupload) support".
 * 
 * @author conesa
 */
@Controller
public class SubmissionQueueController extends AbstractController {

	@Autowired
	private EmailService emailService;
	
	private static Logger logger = Logger.getLogger(SubmissionQueueController.class);

	
	
	@RequestMapping(value = { "/submittoqueue" })
	public ModelAndView preSubmit(HttpServletRequest request) {
		MetabolightsUser user = null;
		
		ModelAndView mav = new ModelAndView("biisubmit"); // Call the Submission form page
		if (request.getUserPrincipal() != null)
			user = (MetabolightsUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

		if (user != null){
			//mav.addObject("user", user);
			
			try {
				mav.addObject("queueditems",SubmissionQueue.getQueuedForUserId(user.getUserName().toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return mav;
	}
	
	
	@RequestMapping(value = "/queueExperiment", method = RequestMethod.POST)
	public ModelAndView queueExperiment(
			@RequestParam("file") MultipartFile file,
			@RequestParam(required=true,value="pickdate") String publicDate,
			@RequestParam(required=false,value="study") String study,
			HttpServletRequest request) 
		throws Exception {

		//Start the submission process...
	    logger.info("Queue Experiment. Start");
	    
	   StringBuffer messageBody = new StringBuffer();
	   String hostName = java.net.InetAddress.getLocalHost().getHostName();
	   messageBody.append("Study submission started from machine " + hostName);
	   
  		// Get the user
		MetabolightsUser user = (MetabolightsUser) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	   
	   try {

            if (file.isEmpty())
				throw new Exception(PropertyLookup.getMessage("BIISubmit.fileEmpty"));

			if (publicDate.isEmpty())
				throw new Exception(PropertyLookup.getMessage("BIISubmit.dateEmpty"));
			
			if (!file.getOriginalFilename().toLowerCase().endsWith("zip")){
				throw new Exception(PropertyLookup.getMessage("BIISubmit.fileExtension"));
			}

            //Check if the study is public today
            VisibilityStatus status = VisibilityStatus.PRIVATE;         //Defaults to a private study

            Date publicDateD;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            publicDateD = sdf.parse(publicDate);               //Date from the form

            if (publicDateD.before(new Date())){  //The date received from the form does not contain time, so this should always be before "now"
                status = VisibilityStatus.PUBLIC;
            }

            // Extend the message...
          	messageBody.append("\nFileName: " + file.getOriginalFilename() );
    		messageBody.append("\nUser: " + user.getUserName() );
    		if (study==null){
    			messageBody.append("\nNEW STUDY");
    		}else{
    			messageBody.append("\nSTUDY: " + study);
    		}
    		messageBody.append("\nPublic Release Date: " + publicDate);
    		
    		
            logger.info("Queueing study");
			SubmissionItem si = new SubmissionItem(file, user, publicDateD, study);
            
			// Submit the item to the queue...
            si.submitToQueue();
            
            messageBody.append("\n\n File Successfully queued.");
            
            logger.info("Queued study. Adding data to session");
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("itemQueued", "msg.studyQueueSuccesfully");
			
			// Cannot load the queue
			emailService.sendQueuedStudyEmail(si.getUserId(),si.getOriginalFileName() , FileUtils.byteCountToDisplaySize(si.getFileQueued().length()), si.getPublicReleaseDate(), hostName, study);
			
			
			
	    	return new ModelAndView("redirect:itemQueued");
	    	
		} catch (Exception e){
			
			ModelAndView mav = new ModelAndView("submitError");
			logger.error(e);
			mav.addObject("error", e);

			messageBody.append("\n\nERROR!!!!!\n\n" + e.getMessage() );
			emailService.sendSimpleEmail( "queueExperiment FAILED in " + hostName + " by " + user.getUserName() , messageBody.toString());
			
			return mav;
		

		}
		

	}
	
	/**
	 * Redirection after a user has successfully submitted. This prevents double submit with F5.
	 */
	@RequestMapping(value={"/itemQueued"})
	public ModelAndView queueComplete(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("index"); // default action for this request, unless the session has candy in it.
		if (request.getSession().getAttribute("itemQueued")!=null) {
			mav = new ModelAndView("queuedOk");
			mav.addObject("msg", request.getSession().getAttribute("itemQueued"));
			request.getSession().removeAttribute("itemQueued");
			

		}
		return mav;
	}

}