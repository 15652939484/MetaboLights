package uk.ac.ebi.metabolights.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for login and related actions.  
 * @author Pablo
 *
 */
@Controller
public class StatisticsController extends AbstractController{


	
	@RequestMapping({"/statistics"})
	public ModelAndView showStatistics() {
	    
		ModelAndView mav = new ModelAndView ("statistics");
		return mav;
	    
    }
	
    
}

