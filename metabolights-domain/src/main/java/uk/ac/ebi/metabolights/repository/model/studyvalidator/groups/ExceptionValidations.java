package uk.ac.ebi.metabolights.repository.model.studyvalidator.groups;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.ac.ebi.metabolights.repository.model.Study;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.DescriptionConstants;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Requirement;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Validation;

import java.util.Collection;

/**
 * Created by kalai on 30/09/15.
 */
@JsonTypeName("ExceptionValidations")
@JsonIgnoreProperties(ignoreUnknown = true)

public class ExceptionValidations extends ValidationGroup {


    public ExceptionValidations(Group group) {
        super(group);
        getValidations().add(new UnexpectedExceptionValidation());
    }

    public ExceptionValidations(Group group, Exception exception) {
        super(group);
        getValidations().add(new UnexpectedExceptionValidation(exception));
    }

    public ExceptionValidations() {

    }

    @Override
    public Collection<Validation> isValid(Study study) {
        setStudy(study);
        for (Validation validation : getValidations()) {
            validation.setPassedRequirement(validation.hasPassed());
            validation.setStatus();
           }
        return getValidations();
    }

    @JsonTypeName("UnexpectedExceptionValidation")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnexpectedExceptionValidation extends Validation {

        public UnexpectedExceptionValidation() {
            super(DescriptionConstants.EXCEPTION, Requirement.MANDATORY, Group.EXCEPTION);
        }

        public UnexpectedExceptionValidation(Exception exception) {
            super(DescriptionConstants.EXCEPTION, Requirement.MANDATORY, Group.EXCEPTION);
            setMessage("We could NOT successfully run all the validations. Some validations might have passed." +
                    " There was an exception during the validation: " +
                    exception.getMessage() + ", " +
                    exception.getClass().getName());
        }

        public UnexpectedExceptionValidation(String description, Exception e) {
            super(description, Requirement.MANDATORY, Group.EXCEPTION);
            setMessage("Something went wrong here: " + e.getMessage());
        }

        @Override
        public boolean hasPassed() {
            return false;
        }
    }


}
