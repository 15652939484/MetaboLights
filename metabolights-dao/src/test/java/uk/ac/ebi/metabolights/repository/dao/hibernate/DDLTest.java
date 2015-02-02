/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2015-Jan-19
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

package uk.ac.ebi.metabolights.repository.dao.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.exception.SQLGrammarException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.metabolights.repository.dao.hibernate.datamodel.SessionWrapper;

import java.util.Properties;

public class DDLTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Before
	public void setUp() throws Exception {

		Configuration configuration = new Configuration();

		// Get property file
		Properties hibernateProperties = new Properties();
		//hibernateProperties.load(SessionUtilTest.class.getResourceAsStream("hibernate.properties"));

		configuration.setProperties(hibernateProperties);

		configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");

		HibernateUtil.initialize(configuration);



	}

	protected void dropTables(){

		SessionWrapper session = HibernateUtil.getSession();
		dropTable(session,Constants.STUDY_USER_TABLE);
		dropTable(session, Constants.STUDIES_TABLE);
		dropTable(session, Constants.USERS_TABLE);

	}

	private void dropTable(SessionWrapper session, String table){

		session.needSession();

		try {
			session.createSQLQuery("DROP TABLE " + table).executeUpdate();
		} catch (SQLGrammarException e) {
			logger.info("Expected exception, when table does not exists");
		}

		session.noNeedSession();


	}

	@Test
	public void testSchemaCreation() throws Exception {


		dropTables();

		// Set it up again
		setUp();


	}


}