/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2015-Feb-24
 * Modified by:   conesa
 *
 *
 * Copyright 2015 EMBL-European Bioinformatics Institute.
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Lite Study to be used as a search result element.
 * User: conesa
 * Date: 24/02/15
 * Time: 09:38
 */
public class LiteStudy extends LiteEntity {

	public enum StudyStatus{
		PRIVATE, PENDING, APPROVED, PUBLIC
	}

	private static final Logger logger = LoggerFactory.getLogger(LiteStudy.class);

	private String studyIdentifier;
	private String title;

	// Database fields
	private Study.StudyStatus studyStatus = Study.StudyStatus.PRIVATE;
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date studyPublicReleaseDate;
	private String obfuscationCode = java.util.UUID.randomUUID().toString();


	// Collections
	private Collection<StudyFactor> factors;
	private Collection<Organism> organism;
	@JsonProperty
	private List<User> users = new ArrayList<>();
	private ObservableList<User> usersObserver = FXCollections.observableList(users);

	public LiteStudy(){

		initialize();
	}

	private void initialize(){

		// Configure the user listener collection.
		listenToUsersListChanges();

	}

	private void listenToUsersListChanges() {

		usersObserver.addListener(new ListChangeListener<User>() {
			@Override
			public void onChanged(Change<? extends User> change) {

				change.next();

				if (change.wasAdded()) {
					//Add the study to the user
					addStudyToUsers(change.getAddedSubList());
				}
			}
		});

	}

	private void addStudyToUsers(List<? extends User> addedUsers) {
		for (User addedUser : addedUsers) {
			addedUser.getStudies().add(this);
		}
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public boolean isPublicStudy() {
		return studyStatus.equals(StudyStatus.PUBLIC);
	}

	public StudyStatus getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(StudyStatus studyStatus) {
		this.studyStatus = studyStatus;
	}

	public Collection<StudyFactor> getFactors() {
		return factors;
	}

	public void setFactors(Collection<StudyFactor> factors) {
		this.factors = factors;
	}

	public Collection<Organism> getOrganism() {
		return organism;
	}

	public void setOrganism(Collection<Organism> organism) {
		this.organism = organism;
	}

	public List<User> getUsers() {
		return usersObserver;
	}

	public void setUsers(List<User> users) {

		// To keep observableList in sync, do not overwrite the users, since the observer will not observe the new list
//		this.users = users;

		usersObserver.clear();
		usersObserver.addAll(users);
	}

	public String getObfuscationCode() {
		return obfuscationCode;
	}

	public void setObfuscationCode(String obfuscationCode) {
		this.obfuscationCode = obfuscationCode;
	}



}