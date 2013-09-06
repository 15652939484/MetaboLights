/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * Last modified: 8/30/13 10:27 AM
 * Modified by:   conesa
 *
 * Copyright 2013 - European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 */

package uk.ac.ebi.metabolights.repository.model;

import java.util.Collection;
import java.util.Date;

/**
 * User: conesa
 * Date: 28/08/2013
 * Time: 11:27
 */
public class Study {
    private String studyIdentifier;
    private Date studyPublicReleaseDate;
    private Date studySubmissionDate;
    private String title;
    private String description;
    private boolean isPublic;

    // Collections
    private Collection<Contact> contacts;
    private Collection<StudyDesignDescriptors> descriptors;
    private Collection<StudyFactor> factors;
    private Collection<Publication> publications;
    private Collection<Protocol> protocols;


    public String getStudyIdentifier() {
        return studyIdentifier;
    }

    public void setStudyIdentifier(String studyIdentifier) {
        this.studyIdentifier = studyIdentifier;
    }

    public Date getStudyPublicReleaseDate() {
        return studyPublicReleaseDate;
    }

    public void setStudyPublicReleaseDate(Date studyPublicReleaseDate) {
        this.studyPublicReleaseDate = studyPublicReleaseDate;
    }

    public Date getStudySubmissionDate() {
        return studySubmissionDate;
    }

    public void setStudySubmissionDate(Date studySubmissionDate) {
        this.studySubmissionDate = studySubmissionDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Collection<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Collection<Contact> colContacts) {
        this.contacts = colContacts;
    }

    public Collection<StudyDesignDescriptors> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(Collection<StudyDesignDescriptors> colDescriptors) {
        this.descriptors = colDescriptors;
    }

    public Collection<StudyFactor> getFactors() {
        return factors;
    }

    public void setFactors(Collection<StudyFactor> factors) {
        this.factors = factors;
    }

    public Collection<Publication> getPublications() {
        return publications;
    }

    public void setPublications(Collection<Publication> publications) {
        this.publications = publications;
    }

    public Collection<Protocol> getProtocols() {
        return protocols;
    }

    public void setProtocols(Collection<Protocol> protocols) {
        this.protocols = protocols;
    }
}
