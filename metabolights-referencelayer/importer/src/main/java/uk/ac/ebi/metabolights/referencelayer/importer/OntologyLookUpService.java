/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 12/3/13 5:30 PM
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
package uk.ac.ebi.metabolights.referencelayer.importer;

import uk.ac.ebi.ws.ols.Query;
import uk.ac.ebi.ws.ols.QueryServiceLocator;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Webservice wrapper for the Ontology LookUp Service http://www.ebi.ac.uk/ontology-lookup/
 *
 * @author pmoreno
 */
public class OntologyLookUpService {
	private static final String NEWT = "NEWT";
	private QueryServiceLocator locator;

    /**
     * Only constructor
     *
     */
    public OntologyLookUpService() {
        this.locator = new QueryServiceLocator();

    }

    /**
     * Will query the OLS and return a map of terms that match a partial term name present in a specific ontology.
     * This search is case insensitive.
     *
     * @param query - string to be searched in the names of the terms
     * @param ontology - the short name given to the ontology (EFO, GO, BTO, etc.) in OLS.
     * @return a map from identifiers to names
     * @throws ServiceException
     * @throws RemoteException
     */
    public Map<String,String> getTermsByName(String query, String ontology) throws ServiceException, RemoteException {
        Query olsQuery = locator.getOntologyQuery();
        return olsQuery.getTermsByName(query, ontology, false);
    }

    /**
     * Will query the OLS and return a map of terms that match a partial term name. The term names will preceded by
     * the shortOntologyName for clarity.
     *
     * @param query - term to query
     * @return a map of terms found. Key will be termId, value will be the prefixed termName.
     * @throws ServiceException
     * @throws RemoteException
     */
    public Map<String,String> getTermsPrefixed(String query) throws ServiceException, RemoteException {
        Query olsQuery = locator.getOntologyQuery();
        return olsQuery.getPrefixedTermsByName(query, false);
    }


    /**
     * Retrieves the name for a particular entry of an ontology, based on its identifier. The identifier should include
     * the prefix, such as BTO:0000309 instead of 0000309. The ontology prefix, except for NEWT!!!
     *
     * @param identifer
     * @param ontologyPrefix
     * @return the name of the entry in the ontology
     * @throws ServiceException
     * @throws RemoteException
     */
    public String getTermName(String identifer, String ontologyPrefix) throws ServiceException, RemoteException {

       // Prepare the query if NEWT:::grrrr
		if (ontologyPrefix.equals(NEWT)){
			identifer = identifer.replace(NEWT + ":", "");
		}

        Query olsQuery = locator.getOntologyQuery();

		return olsQuery.getTermById(identifer, ontologyPrefix);
    }

}
