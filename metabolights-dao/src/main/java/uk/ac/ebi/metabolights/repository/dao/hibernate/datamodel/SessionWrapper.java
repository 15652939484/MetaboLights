/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2015-Jan-26
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

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * User: conesa
 * Date: 26/01/15
 * Time: 14:02
 */
public class SessionWrapper {
	private SessionFactory factory;
	private Session session;
	private int sessionCount = 0;
	public SessionWrapper(SessionFactory factory) {
		this.factory = factory;
	}


	public Session needSession() {

		// If empty..
		if (session ==null) {

			//session = HibernateUtil.getSessionFactory().getCurrentSession();
			// If still empty ...
			//if (session == null){
				session = factory.openSession();
				session.beginTransaction();
			//}
		}

		sessionCount++;
		return session;

	}

	public void noNeedSession() {

		// Decrease the count
		sessionCount--;

		// If 0 close it
		if (sessionCount == 0) {
			session.getTransaction().commit();
			session.close();
			session = null;
		}

	}
	public Query createQuery(String query){

		return session.createQuery(query);
	}

	public void delete(DataModel dataModel) {
		session.delete(dataModel);
	}

	public void saveOrUpdate(DataModel datamodel) {
		session.saveOrUpdate(datamodel);
	}
	public void save(DataModel datamodel) {
		session.save(datamodel);
	}

	public Object get(Class dataModelclass, Long id) {
		return session.get(dataModelclass,id);
	}

	public SQLQuery createSQLQuery(String SQLQuery) {
		return session.createSQLQuery(SQLQuery);
	}
}
