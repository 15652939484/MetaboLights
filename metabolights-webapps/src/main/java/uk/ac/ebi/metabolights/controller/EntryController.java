package uk.ac.ebi.metabolights.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ebi.bioinvindex.model.AssayResult;
import uk.ac.ebi.bioinvindex.model.Study;
import uk.ac.ebi.bioinvindex.model.VisibilityStatus;
import uk.ac.ebi.bioinvindex.model.processing.Assay;
import uk.ac.ebi.bioinvindex.model.security.User;
import uk.ac.ebi.bioinvindex.model.term.FactorValue;
import uk.ac.ebi.bioinvindex.model.term.PropertyValue;
import uk.ac.ebi.metabolights.model.MLAssay;
import uk.ac.ebi.metabolights.properties.PropertyLookup;
import uk.ac.ebi.metabolights.search.LuceneSearchResult;
import uk.ac.ebi.metabolights.service.SearchService;
import uk.ac.ebi.metabolights.service.StudyService;
import uk.ac.ebi.metabolights.service.TextTaggerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * Controller for entry (=study) details.
 * 
 */
@Controller
public class EntryController extends AbstractController {

	private static Logger logger = Logger.getLogger(EntryController.class);
	private final String DESCRIPTION="descr";

	@Autowired
	private StudyService studyService;
	@Autowired
	private SearchService searchService;

    private @Value("#{appProperties.publicFtpLocation}") String publicFtpDirectory;
    private @Value("#{appProperties.privateFtpStageLocation}") String privateFtpDirectory;         //TODO, short term fix until filesystem is mounted RW

	//(value = "/entry/{metabolightsId}")
	@RequestMapping(value = "/{metabolightsId}")
	public ModelAndView showEntry(@PathVariable("metabolightsId") String mtblId, HttpServletRequest request) {
		logger.info("requested entry " + mtblId);

        //if (!mtblId.startsWith("MTBLS") && !mtblId.startsWith("BII")) //Fix, in case the entry controller picks up another page
        //    return new ModelAndView("index", "message", PropertyLookup.getMessage("msg.noPageFound") +mtblId);


        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //TODO, change
        }

        Study study = studyService.getBiiStudy(mtblId,true);
		
		Collection<String> organismNames = new TreeSet<String>();
		for (AssayResult assRes : study.getAssayResults()) {
			for (PropertyValue<?> pv : assRes.getCascadedPropertyValues()) {
				if (pv.getType().getValue().equals("organism")) {
					organismNames.add(pv.getValue());
					//logger.debug("adding "+pv.getValue());
				}
			}
		}

		// Look for metabolites (in the index)
		LuceneSearchResult sr = null;
		
		try {
            List<LuceneSearchResult> srList = null;

            HashMap<Integer,List<LuceneSearchResult>> srStudies = searchService.search(mtblId);
            if (srStudies.size() != 0){
			    srList = (srStudies.values().iterator().next());

                if (srList != null)
			        sr = srList.get(0);
            }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the DownloadLink
		String ftpLocation = getDownloadLink(study.getAcc(), study.getStatus());

		ModelAndView mav = new ModelAndView("entry");
		mav.addObject("study", study);
		mav.addObject("organismNames", organismNames);
		mav.addObject("factors", getFactorsSummary(study));
		mav.addObject("assays", getMLAssays(study));
        if (!study.getAcc().equals(VisibilityStatus.PRIVATE.toString()))    //User is not authorised to view this study
            mav.addObject("ftpLocation",ftpLocation);

        if (sr != null)
            mav.addObject("metabolites", sr.getMetabolites());
        
        //Stick text for tagging (Whatizit) in the session..
        if (study.getDescription()!=null) {
        	logger.debug("placing study description in session for Ajax highlighting");
        	request.getSession().setAttribute(DESCRIPTION,study.getDescription());
        }

		return mav;
	}
	private Collection<MLAssay> getMLAssays(Study study){
		
		HashMap<String,MLAssay> mlAssays = new HashMap<String, MLAssay>();
		
		for (AssayResult ar : study.getAssayResults()){
			
			MLAssay mlAssay= null;
			
			for (Assay assay: ar.getAssays()){
				
				String assayName = MLAssay.getAssayNameFromAssay(assay);
				
				
				// If we don't have the MLAssay
				if (!mlAssays.containsKey(assayName)){
					mlAssay = new MLAssay(assay);
					mlAssays.put(assayName, mlAssay);
				} else {
					mlAssay = mlAssays.get(assayName);
				}
				
				mlAssay.addAssayLines(assay);
				
			}
			
			// Add the assay line to the assay
			mlAssay.addAssayResult(ar);

		}
		return mlAssays.values();
		
	}

	
	@RequestMapping(value = "/privatefiles/{file_name}")
	public void getFile(@PathVariable("file_name") String fileName,	HttpServletResponse response, Principal principal) {
		try {

			Boolean validUser = false;
			final String currentUser = principal.getName(); //The logged in user.  principal = MetabolightsUser

			if (currentUser != null){ //Check if the logged in user can access the study
				Study study = studyService.getBiiStudy(fileName,true);
				Collection<User> users = study.getUsers();
				Iterator<User> iter = users.iterator();
				while (iter.hasNext()){
					User user = iter.next();
					if (user.getUserName().equals(currentUser)){
						validUser = true;
						break;
					}
				}

			}

			if (!validUser)
				throw new RuntimeException(PropertyLookup.getMessage("Entry.notAuthorised")); 

			try {
				// get your file as InputStream
				InputStream is = new FileInputStream(privateFtpDirectory + fileName + ".zip");

				// let the browser know it's a zip file
				response.setContentType("application/zip");

				// copy it to response's OutputStream
				IOUtils.copy(is, response.getOutputStream());
				
			} catch (Exception e) {
				throw new RuntimeException(PropertyLookup.getMessage("Entry.fileMissing")); 
			}

			response.flushBuffer();

		} catch (IOException ex) {
			logger.info("Error writing file to output stream. Filename was '"+ fileName + "'");
			throw new RuntimeException("IOError writing file to output stream");
		}

	}

    @Autowired
    private TextTaggerService textTagger;

    /**
     * Intended for an asynchronous call, to tag the description in a study.
     * @param request
     * @return ugly pixie 
     */
	@RequestMapping(value="/tagText")
	public ModelAndView whatIzItStuff (HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("taggedText");
		String description = (String) request.getSession().getAttribute(DESCRIPTION);
    	if(description!=null) {
			logger.debug("Calling WhatWhatIzIt");
			String taggedContent = textTagger.tagText(description);
			mav.addObject("taggedContent", taggedContent);
			logger.debug(taggedContent);
    	}
    	else 
    		mav.addObject("taggedContent", null);
		return mav;
	}
	
	public String getDownloadLink(String study, VisibilityStatus status){
		
		String ftpLocation = null;
		
		if (status.equals(VisibilityStatus.PRIVATE)){	// Only for the submitter
			ftpLocation = "privatefiles/" + study;  //Private download, file stream
		} else {  //Serve back public ftp link
			ftpLocation = publicFtpDirectory.replaceFirst("/ebi/ftp/","ftp://ftp.ebi.ac.uk/") + study +".zip";
		}
		
		return ftpLocation;
		
	}
	
	private HashMap<String,ArrayList<String>> getFactorsSummary(Study study){
		
		HashMap<String, ArrayList<String>> fvSummary = new HashMap<String, ArrayList<String>>();
		
		// Loop through the assay results
		for (AssayResult ar:study.getAssayResults()){
			
			// For each factor value in Data item
			for (FactorValue fv:ar.getData().getFactorValues()){
				
				
				// ... if we don't have the factor already in the collection
				if (!fvSummary.containsKey(fv.getType().getValue())){
					fvSummary.put(fv.getType().getValue(),new ArrayList<String>());
				}
				
				// Get the values for that factor...
				ArrayList<String> values = fvSummary.get(fv.getType().getValue());
				
				
				String value;
				
				// Add the new value
				if (!(fv.getUnit() == null)){
					value = fv.getValue() + " " + fv.getUnit().getValue();
				} else {
					value = fv.getValue();
				}
				
				if (!values.contains(value)) values.add(value);
				
			}
		}
		
		return fvSummary;
		
		
	}

}
