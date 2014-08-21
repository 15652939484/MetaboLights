/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 11/27/13 3:45 PM
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

package uk.ac.ebi.metabolights.species.core.tools;

import uk.ac.ebi.metabolights.species.model.Taxon;

/**
 * User: conesa
 * Date: 27/11/2013
 * Time: 15:10
 */
public class IPNIParentSearcher implements IParentSearcher {
	private static final String IPNI_PREFIX = "IPNI";

	@Override
	public Taxon getParentFromTaxon(Taxon child) {

		// Since IPNI deals with plants, and doesn't have a webservice...we can return always plant taxon (Viridiplantae)
		// GReat, IPNI hasn't got parent information, therefore it doesn't have such a taxon for viridiplantae.
		// SO,...I'll made one up: IPNI:1
		Taxon madeUpPlants = new Taxon(IPNI_PREFIX + ":1", "Viridiplantae","Green plants","");

		if (child.equals(madeUpPlants)){
			return null;
		} else {
			return madeUpPlants;
		}

	}

	@Override
	public boolean isThisTaxonYours(Taxon orphan) {
		return IPNI_PREFIX.equals(orphan.getPrefix());
	}
}
