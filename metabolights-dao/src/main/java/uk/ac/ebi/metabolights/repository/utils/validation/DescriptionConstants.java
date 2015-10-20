package uk.ac.ebi.metabolights.repository.utils.validation;

/**
 * Created by kalai on 18/09/15.
 */
public class DescriptionConstants {

    //STUDY
    public static final String STUDY_IDENTIFIER = "Study identifier";
    public static final String STUDY_TITLE = "Study title";
    public static final String STUDY_DESCRIPTION = "Study description";
    public static final String STUDY_DESIGN_DESCRIPTORS = "Study design descriptors";
    public static final String STUDY_MAX_ONE = "Maximum only one study found";
    public static final String STUDY_TEXT = "Study text successfully decoded";

    //FACTORS
    public static final String FACTOR_NAME= "Study factors";
    public static final String FACTOR_TYPE = "Study factor type";

    //ORGANISM
    public static final String ORGANISM_NAME= "Organism name";
    public static final String ORGANISM_PART = "Organism part";


    //PUBLICATIONS

    public static final String PUBLICATION_TITLE = "Publication title";
    public static final String PUBLICATION_AUTHORS = "Authors of Publication";
    public static final String PUBLICATION_IDS  = "Publication IDs";


    public static final String STUDY_FACTORS = "Study factors used in the experiment";
    public static final String PROTOCOLS_ALL = "Comprehensive Experimental protocol";
    public static final String PROTOCOLS_MINIMUM = "Minimum Experimental protocol";
    public static final String PROTOCOLS_SAMPLE_COLLECTION = "Sample Collection protocol";
    public static final String PROTOCOLS_TEXT = "Protocols text successfully decoded";


    public static final String ASSAYS = "Assay(s)";
    public static final String ASSAY_PLATFORM = "Assay platform information";
    public static final String ASSAY_FILES = "Assay has raw files referenced";
    public static final String ASSAY_FILES_IN_FILESYSTEM = "Assay referenced raw files are present in filesystem";
    public static final String ASSAY_ALL_MAF_REFERENCE = "All Assays have Metabolite Assignment File referenced";
    public static final String ASSAY_ATLEAST_SOME_MAF_REFERENCE = "Atleast some assays have Metabolite Assignment Files referenced";

    public static final String ASSAY_MAF_FILE = "Metabolite Assignment File is present in study folder";
    public static final String ASSAY_CORRECT_MAF_FILE = "Metabolite Assignment File is of correct format";


    public static final String SAMPLES = "Sample(s)";
    public static final String PUBLICATIONS = "Publication(s) associated with this study";
    public static final String ISATAB_INVESTIGATION = "Isatab investigation file check";


    //OTHER
    public static final String EXCEPTION = "For any study we should be able to run all the validations";

    //FILES
    public static final String MAF_FILE = "Metabolite Identification file";
    public static final String MAF_FILE_ASSAY_CROSSCHECK = "Metabolites reported in Assays";
}
