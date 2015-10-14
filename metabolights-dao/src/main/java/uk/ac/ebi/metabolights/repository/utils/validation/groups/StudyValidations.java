package uk.ac.ebi.metabolights.repository.utils.validation.groups;

import uk.ac.ebi.metabolights.repository.model.Contact;
import uk.ac.ebi.metabolights.repository.model.Study;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Group;
import uk.ac.ebi.metabolights.repository.utils.validation.DescriptionConstants;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Requirement;
import uk.ac.ebi.metabolights.repository.utils.validation.Utilities;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Validation;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by kalai on 18/09/15.
 */
public class StudyValidations implements IValidationProcess {


    @Override
    public String getAbout() {
        return Group.STUDY.toString();
    }

    public Collection<Validation> getValidations(Study study) {
        Collection<Validation> studyValidations = new LinkedList<>();
        studyValidations.add(getStudyTitleValidation(study));
        studyValidations.add(getStudyDescriptionValidation(study));
        studyValidations.add(studyDecodeIsSuccessfulValidation(study));

        return studyValidations;

    }

    public static Validation getStudyTitleValidation(Study study) {
        Validation validation = new Validation(DescriptionConstants.STUDY_TITLE, Requirement.MANDATORY, Group.STUDY);
        if (!Utilities.minCharRequirementPassed(
                study.getTitle(), 15)) {
            validation.setMessage("Study title is too short");
            validation.setPassedRequirement(false);
        }
        validation.setStatus();
        return validation;
    }

    public static Validation getStudyDescriptionValidation(Study study) {
        Validation validation = new Validation(DescriptionConstants.STUDY_DESCRIPTION, Requirement.MANDATORY, Group.STUDY);
        if (!Utilities.minCharRequirementPassed(
                study.getDescription(), 30)) {
            validation.setMessage("Study description is too short");
            validation.setPassedRequirement(false);
        }
        validation.setStatus();
        return validation;
    }

   /*
   TODO: StudyDesignDescriptorsValidation, MinimumStudyValidation
    */

    public static Validation studyDecodeIsSuccessfulValidation(Study study) {
        Validation validation = new Validation(DescriptionConstants.STUDY_TEXT, Requirement.MANDATORY, Group.STUDY);
        String message = "";
        if (Utilities.getNonUnicodeCharacters(study.getTitle()).size() > 0) {
            message += "Study title contains characters that cannot be successfully decoded. \n";
        }
        if (Utilities.getNonUnicodeCharacters(study.getDescription()).size() > 0) {
            message += "Study description contains characters that cannot be successfully decoded. \n";
        }
        if (Utilities.getNonUnicodeCharacters(getContactAsString(study.getContacts())).size() > 0) {
            message += "Study contact contains characters that cannot be successfully decoded. \n";
        }
        if (!message.isEmpty()) {
            validation.setPassedRequirement(false);
            validation.setMessage(message);
        }
        validation.setStatus();
        return validation;

    }

    private static String getContactAsString(Collection<Contact> contacts) {
        String c = "";
        for (Contact contact : contacts) {
            c += contact.getFirstName() +
                    contact.getLastName() +
                    contact.getMidInitial() +
                    contact.getAffiliation() +
                    contact.getAddress() +
                    contact.getRole();
        }
        return c;
    }


}
