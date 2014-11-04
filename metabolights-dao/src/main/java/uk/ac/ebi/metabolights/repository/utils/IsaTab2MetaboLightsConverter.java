/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 10/3/14 9:41 AM
 * Modified by:   kenneth
 *
 * Copyright 2014 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * Y
 * ou may obtain a copy of the License at
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
 * Last modified: 7/2/14 5:09 PM
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

package uk.ac.ebi.metabolights.repository.utils;

import org.isatools.conversion.ArrayToListConversion;
import org.isatools.conversion.Converter;
import org.isatools.isacreator.configuration.FieldObject;
import org.isatools.isacreator.model.Factor;
import org.isatools.isacreator.model.StudyDesign;
import org.isatools.manipulator.SpreadsheetManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.metabolights.repository.dao.filesystem.MzTabDAO;
import uk.ac.ebi.metabolights.repository.model.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: conesa
 * Date: 28/08/2013
 * Time: 11:34
 */
public class IsaTab2MetaboLightsConverter {


	static Logger logger = LoggerFactory.getLogger(IsaTab2MetaboLightsConverter.class);
    /*
    From isaTab specifications:
    "Dates
    Dates should be supplied in the ISO 8601 format “YYYY-MM-DD”."
    */
    private static final String ISA_TAB_DATE_FORMAT = "yyyy-MM-dd";
    private static SimpleDateFormat isaTabDateFormat = new SimpleDateFormat(ISA_TAB_DATE_FORMAT);

    private static final String ASSAY_COLUMN_SAMPLE_NAME = "Sample Name";
    private static final String METABOLITE_ASSIGNMENT_FILE = "Metabolite Assignment File";

    private static MzTabDAO mzTabDAO = new MzTabDAO();
    //Below are for sample tab
    private static final String SOURCE_NAME = "Source Name";
    private static final String CHARACTERISTICS_ORGANISM = "Characteristics[Organism]";
    private static final String CHARACTERISTICS_ORGANISM_PART = "Characteristics[Organism part]";
    private static final String PROTOCOL_REF = "Protocol REF";
    private static final String SAMPLE_NAME = "Sample Name";
    private static final String FACTOR = "Factor";
    private static final String FACTOR_VALUE = "Factor Value[";

	// To improve assay conversion performance...
	private static List<String[]> currentAssaySpreadsheet;
	private static String currentAssayName = "";


	public static Study convert( org.isatools.isacreator.model.Investigation investigation, String studyFolder, boolean includeMetabolites){

        // Convert the study from the ISAcreator model...
        Study metStudy = isaTabInvestigation2MetaboLightsStudy(investigation, studyFolder, includeMetabolites);

        // Convert the authors...
        return metStudy;

    }

    /**
     * Reads and maps the MAF to a MetaboliteAssignment class
     * @param fileName
     * @return MetaboliteAssignment with metabolite data
     */
    private static MetaboliteAssignment getMAF(String fileName){
        //Fully qualified file name, incl path!
        return mzTabDAO.mapMetaboliteAssignmentFile(fileName);
    }

    private static Study isaTabInvestigation2MetaboLightsStudy(org.isatools.isacreator.model.Investigation source, String studyFolder, boolean includeMetabolites){

        // Instantiate new MetaboLights investigation object
        Study metStudy = new Study();


        // Get the first and unique study
        org.isatools.isacreator.model.Study isaStudy = source.getStudies().values().iterator().next();


        // Populate direct study members
        metStudy.setStudyIdentifier(isaStudy.getStudyId());
        metStudy.setTitle(isaStudy.getStudyTitle());
        metStudy.setDescription(isaStudy.getStudyDesc());

        if (isaStudy.getPublicReleaseDate() != null)
            metStudy.setStudyPublicReleaseDate(isaTabDate2Date(isaStudy.getPublicReleaseDate()));

        if (isaStudy.getDateOfSubmission() != null)
            metStudy.setStudySubmissionDate(isaTabDate2Date(isaStudy.getDateOfSubmission()));

        metStudy.setStudyLocation(studyFolder);


        // Now collections
        // Contacts
        metStudy.setContacts(isaTabContacts2MetaboLightsContacts(isaStudy));

        // Study design descriptors
        metStudy.setDescriptors(isaTabStudyDesign2MetaboLightsStudiesDesignDescriptors(isaStudy));

        // Study factors
        metStudy.setFactors(isaTabStudyFactors2MetaboLightsStudyFactors(isaStudy));

        // Publications
        metStudy.setPublications(isaTabPublications2MetaboLightsPublications(isaStudy));

        // Protocols
        metStudy.setProtocols(isaTabProtocols2MetaboLightsProtocols(isaStudy));

        //Assays
        metStudy.setAssays(isaTabAssays2MetabolightsAssays(isaStudy, metStudy, includeMetabolites));

        //Samples
        metStudy.setSamples(isaTabSamples2MetabolightsSamples(isaStudy, metStudy));

        //Organism and Organism part
        metStudy.setOrganism(sampleOrg2organism(metStudy));


        return metStudy;
    }

    private static Collection<Organism> sampleOrg2organism(Study metStudy) {

        Set<Organism> organisms = new HashSet<Organism>();
        List<String> sampleDeDuplication = new ArrayList<String>();

        for (Sample sample: metStudy.getSamples()){
            Organism organism = new Organism();
            organism.setOrganismName(sample.getCharactersticsOrg());
            organism.setOrganismPart(sample.getCharactersticsOrgPart());

            if (!sampleDeDuplication.contains(organism.getOrganismName())) {
                organisms.add(organism);
                sampleDeDuplication.add(organism.getOrganismName());
            }

        }
        return organisms;
    }


    private static Collection<Contact> isaTabContacts2MetaboLightsContacts(org.isatools.isacreator.model.Study isaStudy){

        List<org.isatools.isacreator.model.Contact> isaContacts = isaStudy.getContacts();

        List<Contact> contacts = new LinkedList<Contact>();

        for (org.isatools.isacreator.model.Contact isaContact: isaContacts){
            Contact contact = new Contact();

            contact.setAddress(isaContact.getAddress());
            contact.setAffiliation((isaContact.getAffiliation()));
            contact.setEmail(isaContact.getEmail());
            contact.setFax(isaContact.getFax());
            contact.setFirstName(isaContact.getFirstName());
            contact.setLastName(isaContact.getLastName());
            contact.setMidInitial(isaContact.getMidInitial());
            contact.setPhone(isaContact.getPhone());
            contact.setRole(isaContact.getRole());

            contacts.add(contact);
        }

        return contacts;

    }

    private static Collection<StudyDesignDescriptors> isaTabStudyDesign2MetaboLightsStudiesDesignDescriptors(org.isatools.isacreator.model.Study isaStudy){

        List<StudyDesign> isaStudyDesigns = isaStudy.getStudyDesigns();

        List<StudyDesignDescriptors> descriptors = new LinkedList<StudyDesignDescriptors>();

        for (StudyDesign isaStudyDesign: isaStudyDesigns){

            StudyDesignDescriptors descriptor = new StudyDesignDescriptors();
            descriptor.setDescription(isaStudyDesign.getIdentifier());
            descriptors.add(descriptor);
        }

        return descriptors;

    }


    private static Collection<StudyFactor> isaTabStudyFactors2MetaboLightsStudyFactors(org.isatools.isacreator.model.Study isaStudy){

        List<Factor> isaFactors = isaStudy.getFactors();

        List<StudyFactor> studyFactors = new LinkedList<StudyFactor>();

        for (Factor isaFactor : isaFactors){
            StudyFactor studyFactor = new StudyFactor();
            studyFactor.setName(isaFactor.getFactorName());
            studyFactors.add(studyFactor);
        }

        return studyFactors;

    }

    private static Collection<Sample> isaTabSamples2MetabolightsSamples(org.isatools.isacreator.model.Study isaStudy, Study metStudy){

        List<List<String>> isaSamplesData = isaStudy.getStudySample().getTableReferenceObject().getReferenceData().getData();
        List<Sample> metSamples = new LinkedList<Sample>();

		Map<String, Integer> sampleFieldsMap = getAssayFieldsMap(metStudy, isaStudy.getStudySample());

        for (List<String> isaSamples: isaSamplesData){

            Sample metSample = new Sample();

            Ontology ontology = new Ontology();

            metSample.setSourceName(getIsaLineValue(isaSamples, SOURCE_NAME, sampleFieldsMap));

            metSample.setCharactersticsOrg(ontology.getName(getIsaLineValue(isaSamples,  CHARACTERISTICS_ORGANISM, sampleFieldsMap)));

            metSample.setCharactersticsOrgPart(ontology.getName(getIsaLineValue(isaSamples,  CHARACTERISTICS_ORGANISM_PART, sampleFieldsMap)));

            metSample.setProtocolRef(getIsaLineValue(isaSamples,  PROTOCOL_REF, sampleFieldsMap));

            metSample.setSampleName(getIsaLineValue(isaSamples,  SAMPLE_NAME, sampleFieldsMap));

            metSample.setFactors(isaTabSampleFactors2MetaboLightsSampleFactors( isaSamples, metStudy.getFactors(),sampleFieldsMap));

            metSamples.add(metSample);

        }

        return metSamples;
    }

	private static Collection<AssayLine> isaTabAssayLines2MetabolightsAssayLines(org.isatools.isacreator.model.Assay isaAssay, Assay metAssay, Study metStudy){

		List<List<String>> isaAssaysLines = isaAssay.getTableReferenceObject().getReferenceData().getData();
		List<AssayLine> metAssayLines = new LinkedList<AssayLine>();

		Map<String, Integer> assayFactorsMap = getAssayFieldsMap(metStudy, isaAssay);

		boolean mafResolved = false;

		for (List<String> isaAssayLine: isaAssaysLines){

			AssayLine metAssayLine = new AssayLine();

			metAssayLine.setSampleName(getIsaLineValue(isaAssayLine, ASSAY_COLUMN_SAMPLE_NAME,assayFactorsMap));
			//TODO, all file references end in  " File", have to loop through the assay spreadsheet to find them

//            List<Factors> allFactors = new LinkedList<Factors>();
//            for (StudyFactor factor : metStudy.getFactorsFromIsaLine()){
//                String factorName = FACTOR_VALUE + factor.getName() +"]";
//
//                String assayFactorValue = "";
//                try {
//                    assayFactorValue = isaAssayLine.get(getAssayColumnIndexByFieldName(isaAssay, factorName));
//                } catch (Exception e){
//                    assayFactorValue = null;
//                }
//
//                if (assayFactorValue != null){
//                    Factors factors = new Factors();
//                    factors.setFactorKey(factor.getName());
//                    factors.setFactorValue(assayFactorValue);
//                    allFactors.add(factors);
//
//                }
//
//            }
//
//            if (allFactors != null)
//                metAssayLine.setFactors(allFactors);

			metAssayLine.setFactors(isaTabAssayFactors2MetaboLightsAssayFactors(isaAssayLine,metStudy.getFactors(),assayFactorsMap));

			// If maf has been resolved....
			if (!mafResolved){
				// Set the metabolite assignment file name if not known (aka MAF)
				if (metAssay.getMetaboliteAssignment().getMetaboliteAssignmentFileName() == null) {

					String mafValue = null;

					Integer mafColumnIndex = getAssayColumnIndexByFieldName(isaAssay, METABOLITE_ASSIGNMENT_FILE);

					if (mafColumnIndex == null) {

						mafResolved = true;

					} else {

						mafValue = isaAssayLine.get(mafColumnIndex);

						// If not empty or null
						if (mafValue!= null && !mafValue.equals("")){

							mafResolved = true;

							String mafFileName = metStudy.getStudyLocation() + File.separator + mafValue;

							File mafFile = new File(mafFileName);

							if(mafFile.exists()){
								metAssay.getMetaboliteAssignment().setMetaboliteAssignmentFileName(mafFileName);
							}
						}

					}

				}

			}
			metAssayLines.add(metAssayLine);
		}

		return metAssayLines;

	}


	private static Collection<Factors> isaTabSampleFactors2MetaboLightsSampleFactors( List<String> isaSampleLine, Collection<StudyFactor> metFactors, Map<String, Integer> sampleFieldsMap ) {

		return getFactorsFromIsaLine(isaSampleLine, metFactors, sampleFieldsMap);
    }

	private static Collection<Factors> isaTabAssayFactors2MetaboLightsAssayFactors( List<String> isaAssayLine, Collection<StudyFactor> metFactors,Map<String, Integer> assayFieldsMap) {

		return getFactorsFromIsaLine(isaAssayLine, metFactors, assayFieldsMap);

	}

	private static Collection<Factors> getFactorsFromIsaLine(List<String> isaLine, Collection<StudyFactor> metFactors, Map<String, Integer> isaFieldsMap) {

		Collection<Factors> newFactors = new ArrayList<Factors>();


		for (StudyFactor studyFactor : metFactors) {     //We need to get the factors from the sample record as they may appear in a different order in Sample and Assay sheets


			String factorName = FACTOR_VALUE + studyFactor.getName() + "]";

			try {


				String value = getIsaLineValueNull(isaLine, factorName, isaFieldsMap);

				if (value!= null) {

					Factors factor = new Factors();
					Ontology ontology = new Ontology();
					factor.setFactorKey(studyFactor.getName());
					String ontologyName = ontology.getName(value);
					factor.setFactorValue(ontologyName);
					newFactors.add(factor);
				}

//				int i = 0;
//				for (Map.Entry<String, FieldObject> isaFactorEntrySet : isa2MetFactorMap.entrySet()) {
//					String isaFactorKeys = isaFactorEntrySet.getKey();
//
//					FieldObject isaFactorValue = isaFactorEntrySet.getValue();
//
//					//if (isaFactorValue.getFieldName().startsWith(FACTOR)) {
//					if (isaFactorValue.getFieldName().equals(factorName)) {
//						Factors factor = new Factors();
//						Ontology ontology = new Ontology();
//						factor.setFactorKey(studyFactor.getName());
//						//int colNo = isaFactorValue.getColNo();           //This is the column number in the config file, not the real column number.
//						String ontologyName = ontology.getName(isaLine.get(i));
//						factor.setFactorValue(ontologyName);
//						metFactors.add(factor);
//
//					}
//
//					i++;
//
//				}
			} catch (Exception e) {

				logger.warn("Can not convert isaTab sample factors into MetaboLights factors." + e.getMessage());
			}

		}

		return newFactors;
	}
//	  Not used?: 02-07-2014
//    private static String trimIsaFactorKeys(String isaFactorKeys) {
//
//        String replaceFirst = "Factor Value\\[";
//        String replaceLast = "]";
//        String factorName = isaFactorKeys.replaceFirst(replaceFirst,"");
//        factorName = factorName.replace(replaceLast,"");
//
//        return factorName;
//    }

	// Returns a map to get the value in an assay line based on the name
	private static Map<String,Integer> getAssayFieldsMap(Study metStudy, org.isatools.isacreator.model.Assay assay){

		Map<String,Integer> assayFieldsMap = new HashMap<String, Integer>();

		// For each field in the assay
		for (FieldObject field :  assay.getTableReferenceObject().getFieldLookup().values()){

			Integer index = getAssayColumnIndexByFieldName(assay, field.getFieldName());

			assayFieldsMap.put(field.getFieldName(), index);

		}

		return assayFieldsMap;

	}

    private static int getSampleColumnIndexByFieldName(org.isatools.isacreator.model.Study isaStudy, String sourceName){

        Collection<FieldObject> isaStudyFieldValue = isaStudy.getStudySample().getTableReferenceObject().getFieldLookup().values();
        int colNo = 0;

        if(!isaStudyFieldValue.isEmpty()){
            for(FieldObject fieldValue: isaStudyFieldValue){
                if(fieldValue.getFieldName().equalsIgnoreCase(sourceName)){
                    colNo = fieldValue.getColNo();
                }
            }
        }

        return colNo;
    }

    private List<AssayFiles> getFileNamesFromAssay(org.isatools.isacreator.model.Assay isaAssay){
        List<AssayFiles> fileList = new ArrayList<AssayFiles>();

        return fileList;
    }

	private static String getIsaLineValueNull(List<String> isaLine, String fieldName, Map<String, Integer> isaFieldsMap){

		// Get the index
		Integer index = isaFieldsMap.get(fieldName);

		if (index == null){

			// We return an empty string when field is not found
			return null;
		}


		return isaLine.get(index);
	}


	private static String getIsaLineValue(List<String> isaLine, String fieldName, Map<String, Integer> isaFieldsMap){

		// Get the value
		String value = getIsaLineValueNull(isaLine, fieldName, isaFieldsMap);

        if (value == null){

			// We return an empty string when field is not found
			return "";
		}


		return value;
	}

    private static Integer getAssayColumnIndexByFieldName(org.isatools.isacreator.model.Assay isaAssay, String fieldName) {

		List<String[]> assaySpreadsheet = getCurrentAssaySpreadsheet(isaAssay);

		Collection<Integer> indexes = SpreadsheetManipulation.getIndexesWithThisColumnName(assaySpreadsheet, fieldName, true);

		// Return the first one (there's should be only one...)
        if (indexes != null && indexes.iterator().hasNext())
		    return indexes.iterator().next();

        return null;
	}

	private static List<String[]> getCurrentAssaySpreadsheet(org.isatools.isacreator.model.Assay isaAssay){

		// If not the same current assay
		if (!isaAssay.getAssayReference().equals(currentAssayName)){
			currentAssayName = isaAssay.getAssayReference();

			Object[][] values = isaAssay.getAssayDataMatrix();
			Converter<Object[][], List<String[]>> arrayToListConversion = new ArrayToListConversion();

			currentAssaySpreadsheet = arrayToListConversion.convert(values);

		}

		return currentAssaySpreadsheet;
	}


    private static List<Assay> isaTabAssays2MetabolightsAssays(org.isatools.isacreator.model.Study isaStudy, Study metStudy, boolean includeMetabolites){

        Map<String, org.isatools.isacreator.model.Assay> isaAssays = isaStudy.getAssays();

        List<Assay> assays = new LinkedList<Assay>();

        int i = 1;

        for(Map.Entry<String, org.isatools.isacreator.model.Assay> isaAssayEntry: isaAssays.entrySet() ){

            org.isatools.isacreator.model.Assay isaAssay = isaAssayEntry.getValue();

            Assay metAssay = new Assay();

            metAssay.setFileName(isaAssay.getAssayReference());

            metAssay.setMeasurement(isaAssay.getMeasurementEndpoint());

            metAssay.setPlatform(isaAssay.getAssayPlatform());

            metAssay.setTechnology(isaAssay.getTechnologyType());

            metAssay.setAssayNumber(i); //To enable a simpler URL structure like "MTBLS1/assay/1 or MTBLS2/assay/2

            // Add assay lines
            metAssay.setAssayLines(isaTabAssayLines2MetabolightsAssayLines(isaAssay, metAssay, metStudy));

            // Add the metabolite assignment file (MAF)
            if (includeMetabolites)
                metAssay.setMetaboliteAssignment(
                    getMAF(metAssay.getMetaboliteAssignment().getMetaboliteAssignmentFileName()));

            assays.add(metAssay);
            i++;
        }

        return assays;

    }

    private static Collection<Publication> isaTabPublications2MetaboLightsPublications(org.isatools.isacreator.model.Study isaStudy){

        List<org.isatools.isacreator.model.Publication> isaPublications = isaStudy.getPublications();

        List<Publication> studyPublications = new LinkedList<Publication>();

        for (org.isatools.isacreator.model.Publication isaPublication : isaPublications){
            Publication publication = new Publication();

            publication.setAbstractText(isaPublication.getAbstractText());
            publication.setDoi(isaPublication.getPublicationDOI());
            publication.setPubmedId(isaPublication.getPubmedId());
            publication.setTitle(isaPublication.getPublicationTitle());

            studyPublications.add(publication);
        }


        return studyPublications;

    }

    private static Collection<Protocol> isaTabProtocols2MetaboLightsProtocols(org.isatools.isacreator.model.Study isaStudy){

        List<org.isatools.isacreator.model.Protocol> isaStudyProtocols = isaStudy.getProtocols();

        List<Protocol> studyProtocols = new LinkedList<Protocol>();

        for (org.isatools.isacreator.model.Protocol isaProtocol : isaStudyProtocols){
            Protocol protocol = new Protocol();

            protocol.setName(isaProtocol.getProtocolName());
            protocol.setDescription(isaProtocol.getProtocolDescription());

            studyProtocols.add(protocol);
        }


        return studyProtocols;

    }
    public static Date isaTabDate2Date (String isaTabDate){

        try {
            return isaTabDateFormat.parse(isaTabDate);
        } catch (ParseException e) {
            return null;
        }
    }
}
