/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2015-Mar-11
 * Modified by:   kenneth
 *
 * Copyright 2015 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 6/10/14 11:57 AM
 * Modified by:   conesa
 *
 *
 * ©, EMBL, European Bioinformatics Institute, 2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package uk.ac.ebi.metabolights.metabolightsuploader;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.metabolights.checklists.CheckList;
import uk.ac.ebi.metabolights.checklists.SubmissionProcessCheckListSeed;
import uk.ac.ebi.metabolights.service.AccessionService;
import uk.ac.ebi.metabolights.service.AppContext;
import uk.ac.ebi.metabolights.utils.FileUtil;
import uk.ac.ebi.metabolights.utils.StringUtils;

import javax.naming.ConfigurationException;
import java.io.*;
import java.util.*;


/**
 * IsaTabReplacer
 * It replace StudyIds in ISATabFile by an accession number generated by accesionManager.
 *
 *@author conesa
 */

//@Controller
public class IsaTabIdReplacer
{
	static private Properties props = new Properties();
	static private String pubDateStr;			//Replace str to look for in i_Investigation.txt
	static private String subDateStr;			//Replace str to look for in i_Investigation.txt
	static private String metaboliteProfTypeStr;	//String to search for in i_Investigation.txt, only allow metabolite profiling
	static private String metaboliteProfValueStr;	//String to search for in i_Investigation.txt, only allow metabolite profiling
	static private String fileWithIds;
    static private String newOntologyType;
    static private String newOntologyValue; //This is the new type ontology reference used in ISAcreator 1.7.5+
    static private String sampleFile;
    static private String organism;
    static private String organismPart;
    static private String validateError = "***** You must make sure your study successfully passes the ISAcreator validation (file -> validate ISAtab) before resubmitting your study! *****\n";

	static final String PROP_IDS = "isatab.ids";
	static String[] idList;
	static final String PROP_FILE_WITH_IDS = "isatab.investigationFile";

	private String publicDate; 		//Date from submitter form
	private String submissionDate;	//Date from submitter form
	private Integer singleStudy=0;	//Update when we find study ids in the file

	private String studyIdToUse; // When updating a study, replacement must not be done.

    private static final Logger logger = LoggerFactory.getLogger(IsaTabIdReplacer.class);

	private String isaTabFolder;

    private HashMap<String,String> ids = new HashMap<String,String>();

    //@Autowired
    public AccessionService accessionService;

    public AccessionService getAccessionService() {

    	if (accessionService == null){
    		accessionService = AppContext.getApplicationContext().getBean(AccessionService.class);

    	}
    	return accessionService;
	}

	public String getPublicDate() {
		if (publicDate == null)
			publicDate = "";
		return publicDate;
	}

	public void setPublicDate(String publicDate) {
		this.publicDate = publicDate;
	}

    public String getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getStudyIdToUse(){
        return studyIdToUse;
    }

    public void setStudyIdToUse(String studyIdToUse){
        this.studyIdToUse = studyIdToUse;
    }

	private CheckList cl;

    /**
	 *
	 * @param args
	 * First param must be the file name to work with. It should be a ISATab folder.
     * @throws Exception
	 *
	 */
	public static void main( String[] args ) throws Exception{

		//Check the arguments. 2 is needed.
		if (args.length != 2){
			System.out.println("2 arguments are required. 1: IsaFolder, 2: Submission Date");
			return;
		}

		//There is 1 arguments
		IsaTabIdReplacer itr = new IsaTabIdReplacer();

		//Set the IsaTabArchive
		itr.setIsaTabFolder(args[0]);
		itr.setSubmissionDate(args[1]);

		//Run it
		itr.Execute();

	}

	public IsaTabIdReplacer(String isaTabFolder){
		this.isaTabFolder = isaTabFolder;
	}
	public IsaTabIdReplacer(){

	}

	//IsaTabArchive properties
	public String getIsaTabFolder(){
        return isaTabFolder;
    }

    public void setIsaTabFolder(String isaTabFolder) {
        this.isaTabFolder = isaTabFolder;
    }

	//Ids property
	public HashMap<String,String> getIds(){return ids;}

	public String getIdsNotes(){
		String notes;

		notes = "File " + fileWithIds + " found.";
		//GO through the ids hash
		for (Map.Entry<String,String> entry :ids.entrySet()){
			notes = notes + " Initial Id (" + entry.getKey() + ") has been replaced with metabolights Id (" + entry.getValue() +").";
		}
		return notes;
	}

	//CheckList property
	public void setCheckList(CheckList newCl){cl= newCl;}


	private void loadProperties() throws FileNotFoundException, IOException, ConfigurationException{

		//final String PROPS_FILE = "isatabidreplacer.properties";
        final String PROPS_FILE = "application.properties";

		//If properties are loaded
		if (!props.isEmpty()) {return;}


		logger.info("Loading properties using getClassLoader().getResourceAsStream(" + PROPS_FILE + ")");

		//Load the properties from the property file
		props.load(IsaTabIdReplacer.class.getClassLoader().getResourceAsStream(PROPS_FILE));

		//If property file is empty
		if (props.size() ==0){

			//Dereference
			props = null;

			//Throw an exception
			throw new ConfigurationException("The application.properties file has been found, but it is empty.");
		}

		//Initialise idList
		String ids = props.getProperty(PROP_IDS);
		pubDateStr = props.getProperty("isatab.publicReleaseDate");
		subDateStr = props.getProperty("isatab.studySubDate");
		metaboliteProfTypeStr  = props.getProperty("isatab.profilingType");
		metaboliteProfValueStr = props.getProperty("isatab.profilingValue");
        newOntologyType = props.getProperty("isatab.newOntologyType");
        newOntologyValue = props.getProperty("isatab.newOntologyValue");
        organism = props.getProperty("isatab.organism");
        organismPart = props.getProperty("isatab.organismPart");

		logger.info(PROP_IDS + " property retrieved :" + ids + "," + pubDateStr + "," + subDateStr);

		//Split it by ; to go through the array
	    idList = ids.split(";");

	    //Initialize fileWithIds
	    fileWithIds = props.getProperty(PROP_FILE_WITH_IDS);
        sampleFile = props.getProperty("isatab.sampleFile");
	}

	private void updateCheckList (SubmissionProcessCheckListSeed spcls, String newNotes){

		//If we have a check list
		if (cl != null){
			cl.CheckItem(spcls.getKey(), newNotes);
		}
	}

	public void validateIsaTabArchive () throws IsaTabIdReplacerException{
		String[] msgs = new String[2];
		String msg;

		//Create a File object
		File isatab = new File(isaTabFolder);

		//If file does not exists
		if (!isatab.exists()) {
			//Add the error to msg
			msgs[1]="File " + isaTabFolder + " does not exists.";
		}

		//File must be a folder, if not
		if (!isatab.isDirectory()){
			//Add the error to msg
			msgs[0]= isatab.getName() + " is not a directory.\n";
		}


		//If there are messages (errors)
		msg = org.apache.commons.lang.StringUtils.join(msgs);

		//If there is any message...
		if ( !msg.equals("") ){

			//Throw customize exception...
			IsaTabIdReplacerException e = new IsaTabIdReplacerException("Invalid ISA Tab File:\n", msgs);
			throw e;
		}

		//Check CheckList Item
		updateCheckList(SubmissionProcessCheckListSeed.FILEVALIDATION, "File passed basic validation: correct extensions and files found.");

	}

	public void Execute() throws Exception{

		logger.info("Starting submission upload");

		//Reset id List, it will be populated with the new accession numbers generated
		ids.clear();

		//Load properties
		logger.info("Loading properties");
		loadProperties();

		//Validate
		logger.info("Validating the archive");
		validateIsaTabArchive();

        logger.info("Checking that Organism and Organism Part has been reported, note they can be empty");
        validateOrganismFields();

		//Replace id
		logger.info("Replace study id and study dates");
		replaceIdInFiles();

		// If we are updating a study we have already the id
		if (studyIdToUse != null)
            ids.put(studyIdToUse, studyIdToUse);

		//Update CheckList
		updateCheckList(SubmissionProcessCheckListSeed.IDREPLACEMENTS, getIdsNotes());

	}

    private void validateOrganismFields() throws Exception {
        //Get the sample file
        File isaTabFile = getISAtabFile(sampleFile);

        logger.info("Loading sample file "+isaTabFile.getName());

        checkSampleFields(isaTabFile);

    }

    private void checkSampleFields (File sampleFile) throws Exception{
        try {
            //Use a buffered reader
            BufferedReader reader = new BufferedReader(new FileReader(sampleFile));
            String line =reader.readLine(); //We only want the header line
            String organismError = "Your study does not appear to contain the required field: ";

            if (line != null) {

                if (line.contains(organism)) {
                    if (!line.contains(organismPart)) {
                        bounceError(reader, organismError, organismPart);
                    }
                } else {
                    bounceError(reader, organismError, organism);
                }

                if (line.contains("+"))
                    bounceError(reader, "Bad syntax or incorrect character in header: ", "+");

            }

        } catch (Exception e) {
            throw e;
        }

    }

    private void bounceError(BufferedReader reader, String error, String fieldName) throws Exception{
        String errTxt = error + fieldName + "  \n\n *** PLEASE DO NOT REMOVE ANY COLUMNS ! ***";
            errTxt = errTxt + validateError;         //Todo, read error text from properties
        reader.close();
        logger.error(errTxt);
        System.err.println(errTxt);
        throw new Exception(errTxt);
    }

	private void replaceIdInFiles () throws Exception{

		// Get the investigation file
		File isaTabFile = getISAtabFile(fileWithIds);

		logger.info("Loading investigation file "+isaTabFile.getName());

		// Replace the id
		replaceInFile(isaTabFile);

	}

	/**
	 * @return ISAtab file based on the given filename pattern
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	private File getISAtabFile(String filePattern) throws ConfigurationException, IOException {

		//Search for the investigation file
		File isaFolder = new File(isaTabFolder);
		File[] fileList;

		// Load properties
		loadProperties();

		//Define a filename filter
        FileFilter filter = new RegexFileFilter(filePattern);

		//Get the file list filtered
		fileList = isaFolder.listFiles(filter);

		//If there is not a file...
		if (fileList.length ==0 || fileList == null) {
			throw new FileNotFoundException ("File (" + filePattern + ") not found");
		}

		//There must be only one, so take the first
		return fileList[0];  //Be aware that this only works with investigation (i_) and sample (s_) files
	}

	/**
	 * Replaces Id in a single file. Goes through each line and replace the id if it's the correct line.
	 * @param fileWithId
	 * @throws Exception
	 */
	private void replaceInFile(File fileWithId) throws Exception{

		logger.info("Replacing ids in file -->" + fileWithId.getAbsolutePath());

		// Reset number of studies.
		singleStudy= 0;

        // Do not increase the accession count if we are updating (we have studyToUse)
		//Set the new accession number, one per file  (Investigation Id and Study Id)
        //String accessionNumber = getAccessionService().getAccessionNumber();
		String accessionNumber = studyIdToUse==null?getAccessionService().getAccessionNumber():studyIdToUse;

		try {
			//Use a buffered reader
			BufferedReader reader = new BufferedReader(new FileReader(fileWithId));
			String line = "", text = "";

			//Go through the file
			while((line = reader.readLine()) != null)
			{

				if (!checkIfMetaboliteProfiling(line)){    //Check if this is metabolite profiling
					String errTxt = "Sorry, only metabolite profiling is accepted in MetaboLights";  //Todo, read error text from properties
					reader.close();
					logger.error(errTxt);
					System.err.println(errTxt);
					throw new Exception(errTxt);
				}

				if (singleStudy>1){  //If we already have assigned a study, fail the upload
					String errTxt = "Sorry, Only one study per submission accepted in MetaboLights";  //Todo, read error text from properties
					reader.close();
					logger.error(errTxt);
					System.err.println(errTxt);
					throw new Exception(errTxt);
				}

                if (!newOntologyUsed(line)){
                    String errTxt = "\n<b>This study does not conform to the current ontology setup requirements or it was created with an old version of ISAcreator</b>.\n\n\n";
                        errTxt = errTxt + "Please download the latest version of our ISAcreator bundle and update your study. ";
                        errTxt = errTxt + "Download here: ftp://ftp.ebi.ac.uk/pub/databases/metabolights/submissionTool/ISAcreatorMetaboLights.zip\n\n";
                        errTxt = errTxt + validateError;
                    reader.close();
                    logger.error(errTxt);
                    System.err.println(errTxt);
                    throw new Exception(errTxt);
                }

				//Replace Id in line (it could come with their own identifier, since we now accept those with the same initial identifier), also check for multiple studies reported
				//Pass in the accession number to use for both study and investigation accession (same id per submission)
				line = replaceIdInLine(line, accessionNumber);


				//Replace public release date for this study
				line = replacePubRelDateInLine(line);

				//Replace study submission date for this study
				line = replaceSubmitDateInLine(line);

			    //Add the final carriage return and line feed
				text += line + "\r\n";
			}

			//Close the reader
			reader.close();

			//Save the file
			FileUtil.String2File(text, fileWithId.getPath());
		} catch (Exception e) {
			throw e;
		}

	}

	private String replaceIdInLine(String line, String accessionNumber){

	    //For each id...
	    for (int i=0;i<idList.length;i++) {

	      //Get the value (Study Identifier, Investigation Identifier)
	      String id = idList[i];

	      //If the value is present in line, in the first position.
	      if (line.indexOf(id)==0){

	    	  logger.info("Line with identifiers found: " + line);

	    	  //Get the Id Value (i.e.: BII-1-S)
	    	  String idInitialValue = StringUtils.replace(line, id + "\t\"", "");
	    	  idInitialValue = StringUtils.truncate(idInitialValue);

	    	  //Compose the line:         Study Identifier   "MTBL1"
	    	  line = id + "\t\"" + accessionNumber + "\"";

	    	  //If the value is a study identifier
	    	  //This is necessary for the uploading using command line tools.
	    	  //The accession number list will be used to assign permissions.
	    	  //Permissions can only be done to Study Identifier elements.
	    	  //Only Study Identifier can be linked.
	    	  if ("Study Identifier".equals(id)){

	    		  ++singleStudy;  //Count how many study id's we have processed

	    		//Populate the list of new accession numbers (initialized in Execute method)
				//accessionNumberList = accessionNumberList + accession + " ";
				//initialIdValuesList = initialIdValuesList + idInitialValue + " ";
	    		ids.put(idInitialValue, accessionNumber);
                //setStudyIdToUse(accession);
                getAccessionService().saveSubmittedId(idInitialValue, accessionNumber);
	    		logger.info("Study identifier " + idInitialValue + " replaced with " +accessionNumber);

	    	  }

	    	  return line;
	      }

	    }

	    return line;

	}

	/*
	 * String replace the public release date in i_investigation.txt file
	 */
	private String replacePubRelDateInLine(String line){

	      //If the value is present in line, in the first position.
	      if (line.indexOf(pubDateStr)==0){

	    	  logger.info(pubDateStr + " found in line " + line);

	    	  //Compose the line:Study Public Release Date	"10/03/2009"
	    	  String newLine = pubDateStr + "\t\"" + getPublicDate() + "\"";

	    	  return newLine;

	      } else {
	    	  return line;
	      }

	}

	/*
	 * String replace the MetaboLights submission date in i_investigation.txt file
	 */
	private String replaceSubmitDateInLine(String line){

		//If the value is present in line, in the first position.
		if (line.indexOf(subDateStr)==0){

			logger.info(subDateStr + " found in line " + line);

			//Compose the line:Study Submission Date	"30/04/2007"
			String newLine = subDateStr + "\t\"" + getSubmissionDate() + "\"";

			return newLine;

		} else {
			return line;
		}

	}

	/*
	 * Check if the this is a metabolite profiled study, read from i_investigation.txt file
	 */
    private Boolean checkIfMetaboliteProfiling(String line){

    	//Is this metabolite profiling type and the value is metabolite profiling
    	if ( line.indexOf(metaboliteProfTypeStr + "\t")==0 && !line.contains(metaboliteProfValueStr) ){
    		    logger.error("'"+ metaboliteProfTypeStr + "\t" + "' found, but no '" +metaboliteProfValueStr+ "' in line: " + line);
    		    return false;
    	}

        return true;  //Not the correct line or correct type/value combo

    }

    private Boolean newOntologyUsed(String line){

        //Has this study been created using the new ontology references?  ISAcreator 1.7.5+
        if ( line.indexOf(newOntologyType + "\t")==0 && !line.contains(newOntologyValue) ){
            logger.error("'"+ newOntologyType + "\t" + "' found, no new ontology referenced '" +newOntologyValue+ "' in line: " + line);
            return false;
        }

        return true;  //Not the correct line or correct type/value combo

    }

    /**
     * Replaces values in an ISATab file using the replacementHash,
     *  <LI> it goes through the file</LI>
     *  <LI> search for any field in replacement.keys</LI>
     *  <LI> replaces it with correspondent value</LI>
     * @param replacementHash: Hash where the key is the Tag to search for and the value is the value to write.
     * @throws Exception
     */
    public void replaceFields(HashMap<String,String> replacementHash) throws Exception{

		// Get the investigation file
		File isaTabFile = getISAtabFile(fileWithIds);

		// Replace the id
		replaceFieldsInFile(isaTabFile, replacementHash);


    }

    private void replaceFieldsInFile(File fileWithId, HashMap<String,String> replacementHash) throws Exception{

		logger.info("Replacing fields in file -->" + fileWithId.getAbsolutePath());

		try {
			//Use a buffered reader
			BufferedReader reader = new BufferedReader(new FileReader(fileWithId));
			String line = "", text = "";

			//Go through the file
			while((line = reader.readLine()) != null)
			{

				//Replace fields in file
				line = replaceFieldsInLine(line, replacementHash);

			    //Add the final carriage return and line feed
				text += line + "\r\n";
			}

			//Close the reader
			reader.close();

			//Save the file
			FileUtil.String2File(text, fileWithId.getPath());

		} catch (Exception e) {
			throw e;
		}
	}

    public String getFieldInLine(String line){

    	int tabPos = line.indexOf("\t");

    	// If there isn't any tab
    	if (tabPos == -1){
    		return null;
    	}else{
    		return line.substring(0, tabPos);
    	}

    }
    public String getValueInLine(String line){

    	int tabPos = line.indexOf("\t");

    	// If there isn't any tab
    	if (tabPos == -1){
    		return null;
    	}else{
    		// Return the value without double quotes
    		return line.substring(tabPos+2, line.length()-1);
    	}

    }

    private String replaceFieldsInLine(String line, HashMap<String,String> replacementHash){

    	// Get the field of the line
    	String field = getFieldInLine(line);

    	// If the line has a field
    	if (field != null){

    		// If the field is present in the hash
    		if (replacementHash.containsKey(field)){

    			// Get the value
    			String value = replacementHash.get(field);

    			logger.info("Field found: " + field + " in line " + line + ". Replacing value with " + value );

    			line = field + "\t\"" + value + "\"";
    		}

    	}

    	// Return the line
    	return line;

	}
    /**
     * Get the values corresponding to the fields passed as parameter. Its a basic method,
     * @param fields
     * @return HashMap with the field (key) and the value(value). In case of the field exists twice in the file it will return unpredicted results.
     * @throws Exception
     */
    public Map<String,String> getFields(String[] fields) throws Exception{

		// Get the investigation file
		File isaTabFile = getISAtabFile(fileWithIds);

		// Get Fields in file
		return getFieldsInFile(isaTabFile, fields);

    }

    private Map<String,String> getFieldsInFile(File fileWithId, String[] fields) throws Exception{

    	logger.info("Getting fields in file -->" + fileWithId.getAbsolutePath());

		try {
			//Use a buffered reader
			BufferedReader reader = new BufferedReader(new FileReader(fileWithId));

			String line = "";
			HashMap <String,String> result = new HashMap<String,String>();

			// Convert the array into a Set
			Set<String> fieldsSet = new HashSet<String>(Arrays.asList(fields));

			//Go through the file
			while((line = reader.readLine()) != null)
			{

				//Replace fields in file
				getFieldsInLine(line, fieldsSet, result);

			}

			//Close the reader
			reader.close();

			return result;

		} catch (Exception e) {
			throw e;
		}
    }

    private String getFieldsInLine(String line, Set<String> fields, HashMap<String,String> result){

    	// Get the field of the line
    	String field = getFieldInLine(line);

    	// If the line has a field
    	if (field != null){

    		// If the field is present in the field set
    		if (fields.contains(field)){

    			// Insert the value into the result Hash
    			String value = getValueInLine(line);

    			logger.info("Field found: " + field + " in line " + line + ". Getting value: " + value );

    			//Add a new entry
    			result.put(field, value);
    		}

    	}

    	// Return the line
    	return line;

	}
}
