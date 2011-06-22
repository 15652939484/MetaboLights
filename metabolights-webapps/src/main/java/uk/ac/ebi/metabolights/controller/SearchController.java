package uk.ac.ebi.metabolights.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.ebi.metabolights.search.LuceneSearchResult;
import uk.ac.ebi.metabolights.service.SearchService;

/**
 * 
 * Controller for Metabolights searching.  
 * @author markr
 *
 */
@Controller
public class SearchController extends AbstractController{
	
	private static Logger logger = Logger.getLogger(SearchController.class);
	
	@Autowired
	private SearchService searchService;
	
	private @Value("#{appProperties.urlBiiPrefixSearch}") String urlBiiPrefixSearch;
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST )
	public ModelAndView luceneSearch (HttpServletRequest request) {

	    List<LuceneSearchResult> resultSet = new ArrayList<LuceneSearchResult>();
	    List<String> organisms = new ArrayList<String>();
	    String query = request.getParameter("query");
		try {
			logger.info("searching for "+query);
			resultSet = searchService.search(query);
			logger.debug("Found #results = "+resultSet.size());
			
			Iterator iter = resultSet.iterator();
			while (iter.hasNext()){
				LuceneSearchResult result = (LuceneSearchResult) iter.next();
				if (result.getOrganism()!=null)
					organisms.add(result.getOrganism());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	ModelAndView mav = new ModelAndView("searchResult");
    	mav.addObject("searchResults", resultSet);
    	mav.addObject("userQuery", request.getParameter("query"));
    	if (!query.isEmpty())
    		mav.addObject("userQueryClean", query.replaceAll("\\*", ""));
    	if (!organisms.isEmpty())
    		mav.addObject("organisms", organisms);
    	return mav;
	}
	

}

