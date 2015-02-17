/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 7/1/14 9:28 AM
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

package uk.ac.ebi.metabolights.repository.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Study {

    public enum StudyStatus{
        PRIVATE, PENDING, APPROVED, PUBLIC
    }
    private String studyIdentifier;
    private Date studyPublicReleaseDate;
    private Date studySubmissionDate;
    private String title;
    private String description;
    private String studyLocation;

    // Database fields
    private StudyStatus studyStatus = StudyStatus.PRIVATE;
    private Long id;
    private String obfuscationCode = java.util.UUID.randomUUID().toString();

    // Collections
    private Collection<Contact> contacts;
    private Collection<StudyDesignDescriptors> descriptors;
    private Collection<StudyFactor> factors;
    private Collection<Publication> publications;
    private Collection<Protocol> protocols;
    private List<Assay> assays;
    private Collection<Organism> organism;
    //private Collection<String> owners;
    private Collection<User> users = new ArrayList<>();


	// Tables Sample & Assays
	private Table sampleTable;

    public List<Assay> getAssays() {
        return assays;
    }

    public void setAssays(List<Assay> assays) {
        this.assays = assays;
    }

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

    public boolean isPublicStudy() {
        return studyStatus.equals(StudyStatus.PUBLIC);
    }


    public String getStudyLocation() {
        return studyLocation;
    }

    public void setStudyLocation(String studyLocation) {
        this.studyLocation = studyLocation;
    }

    public StudyStatus  getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(StudyStatus studyStatus) {
        this.studyStatus = studyStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObfuscationCode() {
        return obfuscationCode;
    }

    public void setObfuscationCode(String obfuscationCode) {
        this.obfuscationCode = obfuscationCode;
    }

    // Collections

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

    public Collection<Organism> getOrganism() {
        return organism;
    }

    public void setOrganism(Collection<Organism> organism) {
        this.organism = organism;
    }

//    public Collection<String> getOwners() {
//        return owners;
//    }
//
//    public void setOwners(Collection<String> owners) {
//        this.owners = owners;
//    }

    public Table getSampleTable() {
		return sampleTable;
	}

	public void setSampleTable(Table sampleTable) {
		this.sampleTable = sampleTable;
	}

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

}
