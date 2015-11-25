package uk.ac.ebi.metabolights.repository.model.studyvalidator;


import uk.ac.ebi.metabolights.repository.model.LiteStudy;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kalai on 18/09/15.
 */
public class Validation {

    public Validation(String description, Requirement type) {
        this.description = description;
        this.type = type;
    }

    public Validation(String description, Requirement type, Group group) {
        this.description = description;
        this.type = type;
        this.group = group;
    }

    public Validation() {
    }

    private Integer id;

    private String description;

    private Status status = Status.GREEN;
    private String statusExt = "";

    private Group group;

    private boolean passedRequirement = true;

    private Requirement type = Requirement.MANDATORY;


    private String message = "OK";

    private boolean overriden = false;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getPassedRequirement() {
        return passedRequirement;
    }

    public void setPassedRequirement(boolean passedRequirement) {
        this.passedRequirement = passedRequirement;
        //setStatus();
    }

    public Requirement getType() {
        return type;
    }

    public void setType(Requirement type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public Status getStatus() {
//        return status;
//    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //    public void setStatus() {
//        if (getPassedRequirement()) {
//            setStatus(Status.GREEN);
//            statusExt = (description == null ? "" : description) + " - PASSED";
//        } else {
//            if (getType().equals(Requirement.MANDATORY)) {
//                setStatus(Status.RED);
//                statusExt = (description == null ? "" : description) + " - FAILED";
//            } else {
//                setStatus(Status.ORANGE);
//                statusExt = (description == null ? "" : description) + " - PASSED";
//            }
//        }
//
//    }
    public Status getStatus() {
        if (getPassedRequirement()) {
            return Status.GREEN;
        } else {
            if (getType().equals(Requirement.MANDATORY)) {
                return Status.RED;
            } else {
                return Status.ORANGE;
            }
        }

    }


    public String getStatusExt() {
        return statusExt;
    }


    public boolean isOverriden() {
        return overriden;
    }

    public void setOverriden(boolean overriden) {
        this.overriden = overriden;
    }

}
