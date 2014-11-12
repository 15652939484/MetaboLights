/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2014-Nov-04
 * Modified by:   conesa
 *
 *
 * Copyright 2014 EMBL-European Bioinformatics Institute.
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

/**
 * User: conesa
 * Date: 04/11/14
 * Time: 16:03
 */
public class Field {
	private int index;
	private String header;
	private String fieldType;
	private String description;
	private String cleanHeader;

	public Field(){};
	public Field(String header, int index, String fieldType){
		this.fieldType = fieldType;
		this.header = header;
		this.index = index;
	}
	public int getIndex() {
		return index;
	}
	public String getHeader(){
		return header;
	}
	public String getFieldType(){
		return fieldType;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}

	public void setCleanHeader(String cleanHeader) {
		this.cleanHeader = cleanHeader;
	}

	public String getCleanHeader() {
		return cleanHeader;
	}
}
