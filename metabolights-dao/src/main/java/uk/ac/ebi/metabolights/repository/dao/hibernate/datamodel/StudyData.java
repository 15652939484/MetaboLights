/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2015-Jan-16
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

package uk.ac.ebi.metabolights.repository.dao.hibernate.datamodel;

import uk.ac.ebi.metabolights.repository.dao.hibernate.Constants;
import uk.ac.ebi.metabolights.repository.model.Study;
import uk.ac.ebi.metabolights.repository.model.StudyLite;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User: conesa
 * Date: 16/01/15
 * Time: 11:30
 *
 * This class is meant to represent a row in the study table..it's an intermediate data structure
 * more DB like than the actual Study model.
 */
@Entity
@Table(name = Constants.STUDIES_TABLE)
public class StudyData  extends DataModel<Study> {

	private String acc;
	private String obfuscationcode = java.util.UUID.randomUUID().toString();
	private int status;
	private Set<UserData> users = new HashSet<>();

	public String getAcc() {
		return acc;
	}

	public void setAcc(String acc) {
		this.acc = acc;
	}

	public String getObfuscationcode() {
		return obfuscationcode;
	}

	public void setObfuscationcode(String obfuscationcode) {
		this.obfuscationcode = obfuscationcode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@ManyToMany
	@JoinTable(name="study_user", joinColumns=@JoinColumn(name="studyid"), inverseJoinColumns=@JoinColumn(name="userid"))
	public Set<UserData> getUsers() {
		return users;
	}

	public void setUsers(Set<UserData> users) {
		this.users = users;
	}


	@Override
	protected void setBusinessModelId(Long id) {

		businessModelEntity.setId(id);
	}

	@Override
	protected void businessModelToDataModel() {

		this.id = businessModelEntity.getId();
		this.obfuscationcode = businessModelEntity.getObfuscationCode();
		this.acc = businessModelEntity.getStudyIdentifier();
		this.status = businessModelEntity.getStudyStatus().ordinal();

		// Convert Users...
		this.users = UserData.businessModelToDataModel(businessModelEntity.getUsers());

	}

	@Override
	public Study dataModelToBusinessModel() {

		businessModelEntity = new Study();

		businessModelEntity.setId(this.id);
		businessModelEntity.setObfuscationCode(this.obfuscationcode);
		businessModelEntity.setStudyIdentifier(this.acc);
		businessModelEntity.setStudyStatus(Study.StudyStatus.values()[this.status]);

		// Fill users
		businessModelEntity.setUsers(UserData.dataModelToBusinessModel(users));

		return businessModelEntity;
	}

	public StudyLite studyDataToStudyLite() {

		StudyLite studyLite = new StudyLite();

		studyLite.setStudyId(this.id);
		studyLite.setAccession(this.acc);
		studyLite.setObfuscationCode(this.obfuscationcode);

		return studyLite;
	}

	public static Set<Study> dataModelToBusinessModel(Set<StudyData> dataStudies) {

		Set<Study>studies = new HashSet<Study>();

		for (StudyData studyData :dataStudies){

			Study studyLite = new Study();
			studyLite = studyData.dataModelToBusinessModel();

			// Add it to the collection
			studies.add(studyLite);
		}

		return studies;


	}



	public static Set<StudyLite> studyDataToStudyLite(Set<StudyData> studies) {

		Set<StudyLite>liteStudies = new HashSet<StudyLite>();

		for (StudyData studyData :studies){

			StudyLite studyLite = new StudyLite();
			studyLite = studyData.studyDataToStudyLite();

			// Add it to the collection
			liteStudies.add(studyLite);
		}

		return liteStudies;


	}
}
