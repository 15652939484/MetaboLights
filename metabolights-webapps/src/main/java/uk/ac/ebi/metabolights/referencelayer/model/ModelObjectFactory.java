/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * Last modified: 31/01/14 09:33
 * Modified by:   kenneth
 *
 * Copyright 2014 - European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 */

package uk.ac.ebi.metabolights.referencelayer.model;


import org.apache.log4j.Logger;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.metabolights.referencelayer.DAO.db.*;
import uk.ac.ebi.metabolights.referencelayer.domain.MetaboLightsCompound;
import uk.ac.ebi.metabolights.referencelayer.domain.Pathway;
import uk.ac.ebi.metabolights.referencelayer.domain.SpeciesGroup;
import uk.ac.ebi.metabolights.referencelayer.domain.Spectra;
import uk.ac.ebi.metabolights.service.AppContext;

import java.util.Collection;
import java.util.List;

public class ModelObjectFactory {

	//private static ModelObjectFactory mof;

	private static Logger logger = Logger.getLogger(ModelObjectFactory.class);
	public static Compound getCompound(String accession) {

		try {

			logger.info("Getting compound " + accession);

			// Get the MetaboLights compound DAO
			MetaboLightsCompoundDAO mcd;

			mcd = new MetaboLightsCompoundDAO(AppContext.getConnection());

			MetaboLightsCompound mc = mcd.findByCompoundAccession(accession);

			mcd.close();

            mcd.returnPooledConnection();


			// Get the chebi Entry
			Entity chebiEntity = getCompleteEntityExample(mc.getChebiId());


			Compound comp = new Compound(mc, chebiEntity);


			return comp;


		} catch (Exception e) {

			logger.error(e.getMessage());

			return null;
		}
	}

	private static Entity getCompleteEntityExample (String chebiId){

		Entity entity = null;

	    try {

	      // Create client
	      ChebiWebServiceClient client = new ChebiWebServiceClient();
	      logger.info("Invoking getCompleteEntity");

	      entity = client.getCompleteEntity(chebiId);
	      logger.info("GetName: " + entity.getChebiAsciiName());


	    } catch ( ChebiWebServiceFault_Exception e ) {
	      logger.error(e.getMessage());
	    }

	    return entity;
	}

    public static Spectra getSpectra(long spectraId){
        try {

            MetSpectraDAO specDAO = new MetSpectraDAO(AppContext.getConnection());

            return specDAO.findBySpectraId(spectraId);


        } catch (Exception e) {

            logger.error(e.getMessage());
            return null;
        }


    }

    public static Pathway getPathway(long pathwayId){
        try {

            MetPathwayDAO metPathwayDAO = new MetPathwayDAO(AppContext.getConnection());

            return metPathwayDAO.findByPathwayId(pathwayId);


        } catch (Exception e) {

            logger.error(e.getMessage());
            return null;
        }


    }

	public static Collection<SpeciesGroup> getAllSpeciesTree()
	{
		try {
			SpeciesGroupDAO speciesGroupDAO = new SpeciesGroupDAO(AppContext.getConnection());

			return  speciesGroupDAO.getAll();
		} catch (Exception e) {

			logger.error("Can't retrieve species tree.", e);
			return null;
		}

	}

    /**
     * Get a list of all species name from autocomplete form
     * @return String list of all species names
     */
    public static List<String> getAutoCompleteSpecies(){

        try {
            SpeciesDAO speciesDAO = new SpeciesDAO(AppContext.getConnection());
            return speciesDAO.getAllNamesForAutoComplete();
        } catch (Exception e) {

            logger.error("Can not get the species list for autocomplete function. ", e);
            return null;
        }

    }


    //	public static ModelObjectFactory getInstance(){
//		if (mof == null) mof = new ModelObjectFactory();
//		return mof;
//	}
	private ModelObjectFactory(){}



}
