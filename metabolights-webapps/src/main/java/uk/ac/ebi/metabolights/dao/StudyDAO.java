/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 4/15/14 12:06 PM
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

/**
 *
 */
package uk.ac.ebi.metabolights.dao;


import uk.ac.ebi.bioinvindex.model.Study;

import java.util.List;

/**
 * @author kenneth
 *
 */
public interface StudyDAO {

	public Study getStudy(String studyAcc, boolean clearSession) throws IllegalAccessException;
    public Study getStudy(String studyAcc, boolean clearSession, boolean fromQueueOrReviwer) throws IllegalAccessException;
    public Study getBiiStudyOnObfuscation(String obfuscationCode, boolean clearSession, boolean fromQueueOrReviwer) throws IllegalAccessException;
    public List<String> findAllAcc();
    public List<String> findStudiesGoingLive();
	public void update(Study study);

}
