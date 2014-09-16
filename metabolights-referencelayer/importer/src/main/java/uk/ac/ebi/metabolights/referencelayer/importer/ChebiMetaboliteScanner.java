/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 9/5/14 11:52 AM
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

import org.apache.log4j.Logger;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: conesa
 * Date: 05/09/2014
 * Time: 11:52
 */
public class ChebiMetaboliteScanner {

	public static String  CHEBI_METABOLITE_ROLE = "CHEBI:25212";

	private boolean doFuzzyScan = true;

	private Logger LOGGER = Logger.getLogger(ChebiMetaboliteScanner.class);

	private ChebiWebServiceClient chebiWS = new ChebiWebServiceClient();
//	private ChebiWebServiceClient chebiWS = new ChebiWebServiceClient(new URL("http://www.ebi.ac.uk/webservices/chebi/2.0/webservice?wsdl"),new QName("http://www.ebi.ac.uk/webservices/chebi",	"ChebiWebServiceService"));

	public boolean isDoFuzzyScan() {
		return doFuzzyScan;
	}

	public void setDoFuzzyScan(boolean doFuzzyScan) {
		this.doFuzzyScan = doFuzzyScan;
	}


	private Map<String, Entity> scannedEntityList;

	public Map<String, Entity> scan() throws ChebiWebServiceFault_Exception {
		return scan(CHEBI_METABOLITE_ROLE);
	}

	// Scans chebi looking for any metabolite compound under the specified CHEBI entity.
	public Map<String, Entity> scan(String chebiId) throws ChebiWebServiceFault_Exception {


		// Set final list to null.
		scannedEntityList = new HashMap<String, Entity>();

		addChildrenMetabolitesForChebiID(chebiId);

		return scannedEntityList;

	}


	/*
	Add all children of the chebiId:
		Is_a_tautomer (if structure) --> L-alanine zwiterion IS_TAUTOMER_OF L-alanine
		Is_a  --> Human metabolite IS_A metabolite, L-Alanine IS_A alanine
		Has_role (if no structure?, ask chebi).  --> adenine HAS_ROLE metabolite

	 */
	private void addChildrenMetabolitesForChebiID(String chebiId) throws ChebiWebServiceFault_Exception {


		// Check if that Chebi Id is already in our list
		if (scannedEntityList.containsKey(chebiId)) return ;

		// ...it's not in our list...therefore we add it
		// Get the complete entity
		Entity entity = getChebiEntity(chebiId);

		// Add it to our scanned list to avoid further look ups and endless loops.
		// NOTE: classes will be added too, we may need to clean the list later or have 2 list (scanned and metabolites, or metabolites + classes).
		scannedEntityList.put(chebiId,entity);


		// Now explore children
		List<LiteEntity> children = new ArrayList<LiteEntity>();


		try {

			// Regardless the structure we always do IS_A..
			// ... try tautomers
			List<LiteEntity> is_a = null;
			is_a = getChebiIdsRelatives(chebiId, RelationshipType.IS_A);

			// Add them to the children list
			children.addAll(is_a);


			if (doFuzzyScan) {

				// If entity has a structure we can test for tautomers
				if (entity.getSmiles() != null) {

					List<LiteEntity> structuralChildren = null;

					// ... try tautomers
					structuralChildren = getChebiIdsRelatives(chebiId, RelationshipType.IS_TAUTOMER_OF);

					children.addAll(structuralChildren);

					// ... try acids
					structuralChildren = getChebiIdsRelatives(chebiId, RelationshipType.IS_CONJUGATE_ACID_OF);

					children.addAll(structuralChildren);

					// ... try bases
					structuralChildren = getChebiIdsRelatives(chebiId, RelationshipType.IS_CONJUGATE_BASE_OF);

					children.addAll(structuralChildren);


					// no structure..
				} else {
					// ... try has_role
					List<LiteEntity> roles = null;
					roles = getChebiIdsRelatives(chebiId, RelationshipType.HAS_ROLE);

					children.addAll(roles);

				}

			}


		} catch (ChebiWebServiceFault_Exception e) {
			LOGGER.error("Can't perform fuzzy search of chebiID " + chebiId + " using chebi WS", e);
		}


		// Now we should have all children...
		// Go through the list
		for (LiteEntity child: children){

			// Get again all children...
			addChildrenMetabolitesForChebiID(child.getChebiId());
		}

	}

	private Entity getChebiEntity(String chebiId) throws ChebiWebServiceFault_Exception {
		return chebiWS.getCompleteEntity(chebiId);

	}

	private List<LiteEntity> getChebiIdsRelatives(String chebiId, RelationshipType relType) throws ChebiWebServiceFault_Exception {

		LOGGER.debug("Getting relatives for  " + chebiId + ". Relationship: " + relType.name());

		// Get all the children of that chebi id
		LiteEntityList children = chebiWS.getAllOntologyChildrenInPath(chebiId, relType, true);


		return children.getListElement();

	}

}
