/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2014-Dec-08
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

package uk.ac.ebi.metabolights.search.service.imp.es;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.metabolights.repository.dao.filesystem.StudyDAO;
import uk.ac.ebi.metabolights.repository.model.Study;
import uk.ac.ebi.metabolights.search.service.IndexingFailureException;
import uk.ac.ebi.metabolights.search.service.SearchQuery;
import uk.ac.ebi.metabolights.search.service.SearchResult;
import uk.ac.ebi.metabolights.search.service.imp.es.resultsmodel.LiteStudy;

import java.io.File;

public class ElasticSearchServiceTest {

	ElasticSearchService elasticSearchService = new ElasticSearchService();
	StudyDAO studyDAO;
	private static String ISATAB_CONFIG_FOLDER;
	private static String PRIVATE_FOLDER;
	private static String PUBLIC_FOLDER;


	@Before
	public void init(){


		String studiesFolderName = System.getenv("STUDIES_FOLDER");
		Assert.assertNotNull("STUDIES_FOLDER: Studies folder variable provided.", studiesFolderName);

		PRIVATE_FOLDER = studiesFolderName + "/private";
		PUBLIC_FOLDER = studiesFolderName + "/public";


		ISATAB_CONFIG_FOLDER = System.getenv("ISATAB_CONFIG_FOLDER");

		Assert.assertNotNull("ISATAB_CONFIG_FOLDER: ISA Configuration folder variable provided.", ISATAB_CONFIG_FOLDER);


		studyDAO = new StudyDAO(ISATAB_CONFIG_FOLDER, PUBLIC_FOLDER, PRIVATE_FOLDER);


	}
	@Test
	public void testGetStatus() throws Exception {


		Assert.assertTrue("ElasticSearch service status", elasticSearchService.getStatus());

	}

	@Test
	public void testIndex() throws Exception {


		File studiesFolder = new File(PRIVATE_FOLDER);

		indexFolder(studiesFolder, false);

		studiesFolder = new File(PUBLIC_FOLDER);

		indexFolder(studiesFolder, true);



	}

	@Test
	public void testSearch(){

		SearchQuery query = new SearchQuery("MTBLS1");

		SearchResult<LiteEntity> result = elasticSearchService.search(query);

		Assert.assertTrue("MTBLS1 study is found" ,result.getResults().size() ==1);

		LiteStudy mtbls1 = (LiteStudy) result.getResults().iterator().next();

		Assert.assertEquals("MTBLS1 LiteStudy id populated", "MTBLS1", mtbls1.getStudyIdentifier());
		Assert.assertNotNull("MTBLS1 LiteStudy title populated", mtbls1.getTitle());


	}

	@Test
	public void testDelete() throws IndexingFailureException {

		elasticSearchService.delete("MTBLS1");
	}


	@Test
	public void testResetIndex() throws IndexingFailureException {

		// Call reset index...
		elasticSearchService.resetIndex();

		// Index should be empty but exist and should be configured.
		Assert.assertEquals("Index exists", true, elasticSearchService.doesIndexExists());

		// Index one study
		indexStudy(new File(PUBLIC_FOLDER + "/MTBLS1"), true);



	}

	private void indexFolder(File studiesFolder, boolean publicStudy) throws IndexingFailureException {

		for (File studyFolder:studiesFolder.listFiles()){
			if (studyFolder.isDirectory()){
				indexStudy(studyFolder, publicStudy);
			}
		}
	}

	private void indexStudy(File studyFolder, boolean publicStudy) throws IndexingFailureException {

		// Need to load the study from the Folder
		Study study = studyDAO.getStudy(studyFolder.getName(),false);

		study.setPublicStudy(publicStudy);
		elasticSearchService.index(study);

	}
}