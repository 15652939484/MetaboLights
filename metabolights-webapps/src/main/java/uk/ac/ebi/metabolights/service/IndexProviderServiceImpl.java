/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 5/19/14 1:57 PM
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

package uk.ac.ebi.metabolights.service;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Provides an interface to the Lucene index.<br>
 * Singleton, Spring managed service. 
 */
@Service
public class IndexProviderServiceImpl implements IndexProviderService {

	private static Logger logger = Logger.getLogger(IndexProviderServiceImpl.class);
	private IndexSearcher indexSearcher;
	private IndexReader indexReader;
	private @Value("#{luceneIndexDirectoryShort}bii/") String luceneIndexDirectory;

	public IndexSearcher getSearcher() {
		logger.debug("Singleton hash code is "+this.hashCode());
	
		//Refresh index
		setUp();
		
		return indexSearcher;
		
	}

	
	public IndexReader getReader() {
		logger.debug("Singleton hash code is "+this.hashCode());
	
		//Refresh index if out of date.
		setUp();
				
		return indexReader;
	}
	
	public String getIndexDirectory ()
	{
		return luceneIndexDirectory; 
	}


	/**
	 * Sets up the Lucene index reader and searcher.
	 * Could do this in the constructor, but that Value for the directory 
	 * is not available then, so take care.
	 *  
	 */
	private synchronized void setUp() {
		
		boolean setUpNeeded = false;
		
		try {

			// If indexReader is null
			if (indexReader == null){
				setUpNeeded = true;
			// Or is not up to date.
			}else if (!indexReader.isCurrent()){
				setUpNeeded = true;
			}
		
			if (setUpNeeded) {
				logger.info("Using index directory "+getIndexDirectory());
					FSDirectory directory= FSDirectory.getDirectory(getIndexDirectory());
					
					//Do we need a lock ? Must read the manual.
					//directory.setLockFactory(new SimpleFSLockFactory());
					
					indexSearcher =new IndexSearcher(directory );
					indexReader = IndexReader.open(directory, true);
			}
		} catch (Exception e) {
				e.printStackTrace();
				logger.error(TextUtils.getErrorStackAsHTML(e));
				logger.error("MAJOR ERROR - could not instantiate Lucene index");
		}
	}
}
