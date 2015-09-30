package uk.ac.ebi.metabolights.repository.model.studyvalidator.groups;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.ac.ebi.metabolights.repository.model.LiteStudy;
import uk.ac.ebi.metabolights.repository.model.Publication;
import uk.ac.ebi.metabolights.repository.model.Study;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.DescriptionConstants;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Requirement;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Utilities;
import uk.ac.ebi.metabolights.repository.model.studyvalidator.Validation;

import java.util.Collection;

/**
 * Created by kalai on 18/09/15.
 */
@JsonTypeName("PublicationValidation")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicationValidation extends ValidationGroup {

    Publication publication;

    public PublicationValidation(Group group) {
        super(group);
        getValidations().add(new PublicationTitleValidation());
        getValidations().add(new PublicationAuthorValidation());
        getValidations().add(new PublicationIDsValidation());
    }

    public PublicationValidation(){

    }

    @Override

    public Collection<Validation> isValid(Study study) {
        setStudy(study);
        for (Validation validation : getValidations()) {
            validation.setPassedRequirement(validation.hasPassed());
            validation.setStatus();
            validation.setMessage("Hello");
        }
        return getValidations();
    }

    @JsonTypeName("PublicationTitleValidation")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicationTitleValidation extends Validation {
        @JsonCreator
        public PublicationTitleValidation() {
            super(DescriptionConstants.PUBLICATION_TITLE, Requirement.MANDATORY, Group.PUBLICATION);
        }

        @Override
        public boolean hasPassed() {
            if (!getStudy().getPublications().isEmpty()) {
                for (Publication publication : getStudy().getPublications()) {

                    if (!Utilities.minCharRequirementPassed(publication.getTitle(), 15)) {
                        return false;
                    }
                }

            } else {
                return false;
            }
            return true;
        }
    }

    @JsonTypeName("PublicationAuthorValidation")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicationAuthorValidation extends Validation {
        @JsonCreator
        public PublicationAuthorValidation() {
            super(DescriptionConstants.PUBLICATION_AUTHORS, Requirement.MANDATORY, Group.PUBLICATION);
        }

        @Override
        public boolean hasPassed() {
            return true;
        }
    }

    @JsonTypeName("PublicationIDsValidation")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicationIDsValidation extends Validation {
        @JsonCreator
        public PublicationIDsValidation() {
            super(DescriptionConstants.PUBLICATION_AUTHORS, Requirement.OPTIONAL, Group.PUBLICATION);
        }

        @Override
        public boolean hasPassed() {
            if (!getStudy().getPublications().isEmpty()) {
                for (Publication publication : getStudy().getPublications()) {
                    if (publication.getPubmedId().isEmpty()) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
    }

}
