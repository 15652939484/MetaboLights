package uk.ac.ebi.metabolights.repository.utils.validation.groups;

import uk.ac.ebi.metabolights.repository.model.Protocol;
import uk.ac.ebi.metabolights.repository.model.Study;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Group;
import uk.ac.ebi.metabolights.repository.utils.validation.DescriptionConstants;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Requirement;
import uk.ac.ebi.metabolights.repository.utils.validation.Utilities;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kalai on 01/10/15.
 */

public class ProtocolValidations implements IValidationProcess {

    @Override
    public String getAbout() {
        return Group.PROTOCOLS.toString();
    }

    public  Collection<Validation> getValidations(Study study) {
        Collection<Validation> protocolValidations = new LinkedList<>();
        protocolValidations.add(getMinimumProtocolValidation(study));
        protocolValidations.add(getComprehensiveProtocolValidation(study));
        return protocolValidations;

    }


    public static Validation getMinimumProtocolValidation(Study study) {
        Validation validation = new Validation(DescriptionConstants.PROTOCOLS_MINIMUM, Requirement.MANDATORY, Group.PROTOCOLS);
        if (!study.getProtocols().isEmpty()) {
            int notPassed = 0;
            int passed = 0;
            for (Protocol protocol : study.getProtocols()) {
                if (!Utilities.minCharRequirementPassed(protocol.getDescription(), 3)) {
                    notPassed++;
                } else {
                    passed++;
                }
            }
            if (notPassed == study.getProtocols().size()) {
                validation.setMessage("No details provided");
                validation.setPassedRequirement(false);
            }
            if (passed < 4) {
                validation.setMessage("Not enough protocols has descriptions");
                validation.setPassedRequirement(false);
            }
        } else {
            validation.setMessage("Protocols is empty");
            validation.setPassedRequirement(false);
        }
        validation.setStatus();
        return validation;

    }


    public static Validation getComprehensiveProtocolValidation(Study study) {
        Validation validation = new Validation(DescriptionConstants.PROTOCOLS_ALL, Requirement.OPTIONAL, Group.PROTOCOLS);
        if (!study.getProtocols().isEmpty()) {
            List<String> emptyProtocolFields = new ArrayList<>();
            for (Protocol protocol : study.getProtocols()) {
                if (!Utilities.minCharRequirementPassed(protocol.getDescription(), 3)) {
//                    validation.setMessage("Protocol description is not sufficient or not all required fields are provided. Example:"
//                            + "\"" + protocol.getName() + "\" is either not provided or not sufficiently described.");
                    emptyProtocolFields.add(protocol.getName());
//                    validation.setPassedRequirement(false);
                }
            }
            if (emptyProtocolFields.size() > 0) {
                validation.setPassedRequirement(false);
                validation.setMessage(getErrMessage(emptyProtocolFields));
            }
        } else {
            validation.setMessage("Protocols is empty");
            validation.setPassedRequirement(false);
        }
        validation.setStatus();
        return validation;
    }

    private static String getErrMessage(List<String> emptyProtocolFields) {
        String errMessage = "Protocol description is not sufficient or not all required fields are provided. Missing field(s):";
        for (int i = 0; i < emptyProtocolFields.size(); i++) {
            errMessage += " " + emptyProtocolFields.get(i);
            if (i < emptyProtocolFields.size() - 1) {
                errMessage += ",";
            }
        }
        //errMessage += " has not been described.";
        return errMessage;
    }

    public static boolean metaboliteIdentificationProtocolIsPresent(Study study) {
        if (study.getProtocols().isEmpty()) {
            return false;
        }
        for (Protocol protocol : study.getProtocols()) {
            if (protocol.getName().equals("Metabolite identification")) {
                if (protocol.getDescription().length() > 3) {
                    return true;
                }
            }
        }
        return false;
    }
}

