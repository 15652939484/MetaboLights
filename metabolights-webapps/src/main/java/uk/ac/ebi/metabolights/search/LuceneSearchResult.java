package uk.ac.ebi.metabolights.search;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import uk.ac.ebi.bioinvindex.search.hibernatesearch.StudyBrowseField;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stores a BII Lucene Document and wraps it with accessors.
 * @author markr
 */
public class LuceneSearchResult {

	private static Logger logger = Logger.getLogger(LuceneSearchResult.class);
	
	private List<Assay> assays;
	private HashMap<String,Set<String>> factors;
	private HashMap<String,Set<String>> properties;
	private List<Publication> publications;
	private List<String> technologies;
	private List<String> platforms;
	private List<Metabolite>metabolites;
	private Document doc;
	private float score;
	private Submitter submitter;
	private boolean isPublic;
	private Date releaseDate;
	private Date submissionDate;	
		
	public LuceneSearchResult(Document doc, float score) {
		this.doc=doc;
		this.score=score;
		//Call version 13 this.assays=parseAssays();
		this.assays = parseAssays();
		
		//Field is called factors in the 1.3 version (not in the enum..yet)
		this.factors=parseKeyValue("factors");
		this.properties=parseKeyValue("characteristics");
		//No longer exist in 1.3 index version:this.technologies=getValues("assay_technology_name");
		
		this.publications = parsePublications();
		
		this.platforms=getValues("assay_platform");
		
		this.submitter = parseSubmitter();
		
		this.isPublic = doc.get("status").equals("PUBLIC");
		
			
		this.releaseDate = parseDate("releaseDate");
		this.submissionDate = parseDate("submissionDate");
		
		this.metabolites= parseMetabolites();
		
	}
	
	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getAccInvestigation() {
		return doc.get(StudyBrowseField.INVESTIGATION_ACC.getName());
	}

	public String getAccStudy() {
		return doc.get(StudyBrowseField.STUDY_ACC.getName());
	}
	
	public String getTitle() {
		return doc.get(StudyBrowseField.TITLE.getName());
	}

	public String getOrganism() {
		return doc.get(StudyBrowseField.ORGANISM.getName());
	}
	
	public HashMap<String,Set<String>> getProperties() {
		return properties;
	}
	
	public List<String> getTechnologies() {
		return technologies;
	}
	
	public List<String> getPlatforms() {
		return platforms;
	}

	public HashMap<String,Set<String>> getFactors() {
		return factors;
	}

	public List<Assay> getAssays() {
		return assays;
	}

	public Submitter getSubmitter() {
		return this.submitter;
	}

	public boolean getIsPublic(){
		return this.isPublic;
	}

	public String getDescription() {
		return doc.get("description");
	}
	
	public String getDesign() {
		return doc.get("design_value");
	}	
	
	public void setScore(float score) {
		this.score = score;
	}
	
	public float getScore() {
		return score;
	}

	public Date getReleaseDate(){
		return this.releaseDate;
	}

	public Date getSubmissionDate(){
		return this.submissionDate;
	}
	
	public List<Publication> getPublications() {
		return publications;
	}
	public List<Metabolite> getMetabolites(){
		return metabolites;
	}

	/**
	 * Holds assay information related to a study.
	 * @author markr
	 *
	 */
	public class Assay {
		
		public String getMeasurement() {
			return measurement;
		}
		public void setMeasurement(String measurement) {
			this.measurement = measurement;
		}
		public String getTechnology() {
			return technology;
		}
		public void setTechnology(String technology) {
			this.technology = technology;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		private String measurement;
		private String technology;
		private int count;
		
		public Assay() {
		}

		public Assay(String measurement, String technology, int count) {
			this.measurement=measurement;
			this.technology=technology;
			this.count=count;
		}
	}
	
	/*
	 * Publication information for a study, for Lucene parsing
	 * @author kenneth
	 */
	public class Publication {
		
		private String title;
		private String authors;
		private String pubmedId;
		private String doi;
		
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAuthors() {
			return authors;
		}

		public void setAuthors(String authors) {
			this.authors = authors;
		}

		public String getPubmedId() {
			return pubmedId;
		}

		public void setPubmedId(String pubmedId) {
			this.pubmedId = pubmedId;
		}

		public String getDoi() {
			return doi;
		}

		public void setDoi(String doi) {
			this.doi = doi;
		}

		public Publication() {
		}
		
		public Publication(String title, String authors, String pubmedId, String doi) {
			this.title = title;
			this.authors = authors;
			this.pubmedId = pubmedId;
			this.doi = doi;
		}
	}
	
	public class Submitter{
		private String userName, forename, surname, email;
		public Submitter(){};
		public Submitter(String userName, String forename, String surname, String email){
			this.userName = userName;
			this.forename = forename;
			this.surname= surname;
			this.email=email;
		}

		public String getName(){ return this.forename;}
		public String getSurname(){return this.surname;}
		public String getEmail(){return this.email;}
		public String getUserName(){return this.userName;}
	}
	public class Metabolite{
		private String identifier, description;
		public Metabolite(String identifier, String description){
			this.identifier = identifier;
			this.description= description;
		}
		public String getIdentifier(){return this.identifier;}
		public String getDescription(){return this.description;}
		public String getLink(){
			return inferLink();
		}
		/*
		 * Infers the link that will take the user to the Reference repository.
		 */
		private String inferLink(){
			
			String PRIORITYIDPATTERNS = "^CHEBI:[0-9]+$~^HMDB[0-9]+$~^LM[A-Z]{2}[0-9]+$~^C[0-9]{5}$";
			String ACCESSION_URLS =  "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=" +
						  "~http://www.hmdb.ca/metabolites/" +
						  "~http://www.lipidmaps.org/data/LMSDRecord.php?LMID=" +
						  "~http://www.genome.jp/dbget-bin/www_bget?cpd:";
			
	    	/**
	    	 * URL Samples for ID:
	    	 * CHEBI:		http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:15365			(Whole value)
	    	 * HMDB:		http://www.hmdb.ca/metabolites/HMDB03459							(Whole value)
	    	 * LIPIDMAPS:	http://www.lipidmaps.org/data/LMSDRecord.php?LMID=LMFA01010001		(Whole Value)
	    	 * KEGG:		http://www.genome.jp/dbget-bin/www_bget?cpd:C01401					(Whole Value)
	    	 */
			
			
			
	    	// If value is not null
	    	if (this.identifier != null){
	    		
	    		String[] remotePriorityPatterns = PRIORITYIDPATTERNS.split("~");
	    		String[] remoteAccessionURL = ACCESSION_URLS.split("~");
	    		
	    		for (int i =0; i <remotePriorityPatterns.length; i++){
	    			
	    			// Get the pattern
	    			String pattern = remotePriorityPatterns[i];
	    			
	    			// If the value matches the pattern...
	    			if (this.identifier.matches(pattern)){
	    				
	    				// Get the url
	    				String url = remoteAccessionURL[i];
	    				
	    				// If the url is not empty...
	    				if (!url.isEmpty()){
	    					
	    					// Append the id at the end...
	    					url = url + this.identifier;
	    					
	    					// Return the link..
	    					return url;
	    				}
	    			}
	    		}
	    	}
	    	
	    	return "";
		} // End method
	} // End Class
	
	/*
	 * Parser the metabolites present in the index
	 * FORMAT: description~identfier
	 * SAMPLE: adenosin~CHEBI:12345
	 * 		valid: null~CHEBI:12345, adenosin~null
	 * 		invalid: null~null, adenosin~, ~CHEBI:12345.
	 */
	private ArrayList<Metabolite> parseMetabolites(){
		
		
		String [] metabolites = this.doc.getValues("Metabolite");
		ArrayList <Metabolite> metList = new ArrayList<Metabolite>();
		
		for (String metabolite:metabolites){
			
			// Get the values splitted .
			String[] metaboliteValues = metabolite.split("~");
			
			// Get the values
			// Description
			String description = metaboliteValues[0].toLowerCase(); //TODO, testing if this will make searching better
			// Identifier
			String identifier = metaboliteValues[1];
			
			// Avoid null strings:
			if (description.equals(null)) description = "";
			if (identifier.equals(null)) identifier = "";
			

			// If it is not an unknown
			if (!description.toLowerCase().startsWith("unk")){
			
				// Create and add the metabolite to the list
				metList.add(new Metabolite(identifier,description));
			}
		}
		
		return metList;
	
	}
	
	/*Parses the user field in the lucene index (there must be only one)
	 * Sample:  username:conesa|forename:Pablo|surname:Conesa|email:conesa@ebi.ac.uk
	 */
	private Submitter parseSubmitter(){
		
		//Get the user field from the lucene index
		String submitterString = this.doc.get("user");
		
		//If null
		if (submitterString == null) {return new Submitter();}
		
		//System.out.println("Parsing " + submitterString);
		//Remove "captions:"
		submitterString= submitterString.replace("username:", "").replaceAll("forename:", "").replace("surname:","").replaceAll("email:", "");
		
		//Now we should have something like this: conesa|Pablo|Conesa|conesa@ebi.ac.uk
		//Attention: the split parameter is a regular expression. As it, the pipe means null and therefore it split one character by one
		//http://hoskinator.blogspot.com/2006/11/trouble-using-pipe-with-stringsplit.html
		String[] values = submitterString.split("\\|");
		
		//Create submitter...
		return new Submitter(values[0],values[1],values[2],values[3]);
		
		
	}
	// Parses a date field inside a lucene index. An example is: 20041011230000000
	private Date parseDate(String dateField) {
		
				
		// Get the formatter
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		
		//Get the value of the field
		String date = doc.get(dateField);
		
		try {
		
			//If exists...
			if (date != null){
			
				//Get only the first 8 characters of the string
				date = date.substring(0, 8);

				return formatter.parse(date);

			}else{
				
				// It may be null when the field is not present in the index
				return null;
			}
			
		} catch (Exception e) {
			//This should never happen
			logger.info("Field " + dateField + " with value " + date + " can not be parsed into a date.");
			return null;
		}
		
		
		
	}
	
	private List<Publication> parsePublications() {

		List<Publication> pubList = new ArrayList<Publication>();
		String pubTitle = "", pubAuthors = "", pubmedId ="", doi="";
		int idx = 0;

		String publicationString = doc.get("publication");  //TODO, can have more than one publication
		if (publicationString==null)
			return pubList;
		
		publicationString = publicationString.replaceAll("title:", "").replaceAll("authors:", "").replaceAll("pubmed:", "").replaceAll("doi:doi:", "doi:");
		// Lucene field publication contains "title:zxc:as:vxzvc|authors:lots, of, authors|pubmed:123123|doi:doi:123.1232/asdf"

		StringTokenizer tokzr = new StringTokenizer(publicationString, "|");

		while (tokzr.hasMoreElements()) {
			String token = (String) tokzr.nextElement();
			switch (idx) {
				case 0: pubTitle = token; break; 	
				case 1: pubAuthors = token; break; 
				case 2: pubmedId = token; break; 	
				case 3:	doi = token; break; 		
			}
			idx++;
		}
		
		Publication publication = new Publication(pubTitle, pubAuthors, pubmedId, doi);
		pubList.add(publication);

		return pubList;

	}
	
	
	
	/**
	 * Why is a Set necessary? Because there are duplicates in the result. Like the same factor over and over.. 
	 * @param fieldName
	 * @return
	 */
	private List<String> getValues (String fieldName) {
		SortedSet<String> set = new TreeSet<String>(); 
		String[] values = doc.getValues(fieldName);
		for (String val :values) {
			
			set.add(val);
		}
		return Arrays.asList(set.toArray(new String[set.size()]));
		
	}

	/**
	 * Parse ASSAY field based in the 1.3 version of the lucene index implementation in BII:
	 * Sample: assay(transcription profiling|DNA microarray|14):?xref(E-MAXD-4->AE:RAW):?xref(E-MAXD-4->AE:WEB)
	 * @return
	 */
	private List<Assay> parseAssays() {
		List<Assay> assays = new ArrayList<Assay>();
		String[] assayStrings = doc.getValues(StudyBrowseField.ASSAY_INFO.getName());
		for (String assayString :assayStrings) {
			
			//CREATE THE PATTERN			
            Pattern pattern = Pattern.compile("assay\\(([^\\|]*)\\|([^\\|]*)\\|(\\d*)\\):"); 

            Matcher matcher = 
            pattern.matcher(assayString);

            //Find groups...
            matcher.find(); 
            
            //Get group 1,2,3
			String measurement=matcher.group(1);
			String technology=matcher.group(2);
			int count=Integer.parseInt(matcher.group(3));
			
			Assay assay = new Assay(measurement,technology,count);
			assays.add(assay);
		}
		return assays;
	}

	private HashMap<String,Set<String>> parseKeyValue(String fieldName){

		HashMap<String,Set<String>> keyValue = new HashMap<String,Set<String>>();
		String[] keyValuesString = doc.getValues(fieldName);
		for (String keyValueString :keyValuesString) {
			StringTokenizer tokzr = new StringTokenizer(keyValueString, "[]");
			int idx=0;
			String key="";
			String value="";

			while (tokzr.hasMoreElements()) {
				String token = (String) tokzr.nextElement();
				switch (idx) {
				case 0 : key=token; break;
				case 1 : value=token; break;
				}
				idx++;
			}

            //Quickfix for problem with ":?" values
            value = value.replaceAll(":\\?",":"); //TODO, fix in lucene index builder

			//If there is a key already
			if (keyValue.containsKey(key)){
				
				//Concatenate the value
				keyValue.get(key).add(value);
			}else{
				//New factor...
				Set<String> newSet = new HashSet<String>();
				newSet.add(value);
				keyValue.put(key, newSet);
			}
			
		}
		return keyValue;

		
	}
	
	private String lineSep = System.getProperty("line.separator");
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Score: "+getScore()+lineSep);
		sb.append("Study: "+getAccStudy()+lineSep);
		sb.append("Inv: "+ getAccInvestigation()+lineSep);
		sb.append("Title:"+getTitle()+lineSep);
		sb.append("Organism:"+getOrganism()+lineSep);
		
		for (Entry<String,Set<String>> property : getProperties().entrySet()) {
			sb.append (" -property: "+property.getKey() + ": ");
			for (String value : property.getValue()){
				sb.append(value + ",");
			}
			sb.append (lineSep);
		}
		
		for (Entry<String,Set<String>> factor : getFactors().entrySet()) {
			sb.append (" -factor: "+factor.getKey() + ": ");
			for (String value : factor.getValue()){
				sb.append(value + ",");
			}
			sb.append (lineSep);
		}
		for (LuceneSearchResult.Assay ass : getAssays()) {
			sb.append(" -assay: "+ass.getMeasurement()+" - "+ass.getTechnology()+" - "+ass.getCount()+lineSep);
		}
		return sb.toString();
	}

}
