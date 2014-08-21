/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 6/13/14 4:37 PM
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

package uk.ac.ebi.metabolights.webservice.model;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * User: conesa
 * Date: 13/06/2014
 * Time: 14:55
 */
@Entity
@Table(name = "STUDY")
public class StudyLite {

	@Id
	@Column(name="ID")

	private Long studyId;

	@Column(name="ACC")
	@NotEmpty
	private String accesion;

	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

	public String getAccesion() {
		return accesion;
	}

	public void setAccesion(String accesion) {
		this.accesion = accesion;
	}
}
