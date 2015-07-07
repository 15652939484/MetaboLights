/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 5/1/14 3:31 PM
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

package uk.ac.ebi.metabolights.webservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml_cml.schema.cml2.react.Reaction;
import uk.ac.ebi.cdb.webservice.*;
import uk.ac.ebi.chebi.webapps.chebiWS.model.DataItem;
import uk.ac.ebi.metabolights.referencelayer.IDAO.DAOException;
import uk.ac.ebi.metabolights.referencelayer.importer.ReferenceLayerImporter;
import uk.ac.ebi.metabolights.referencelayer.model.Compound;
import uk.ac.ebi.metabolights.referencelayer.model.Pathway;
import uk.ac.ebi.metabolights.referencelayer.model.Spectra;
import uk.ac.ebi.metabolights.webservice.services.ModelObjectFactory;
import uk.ac.ebi.metabolights.webservice.utils.FileUtil;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.rhea.ws.client.RheasResourceClient;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



/**
 * Controller for reference layer compounds (=MTBLC*) details.
 *
 */
@Controller
@RequestMapping("compounds")
public class CompoundController extends BasicController {

    private static Logger logger = LoggerFactory.getLogger(CompoundController.class);
    private @Value("#{EUPMCWebServiceURL}") String PMCurl;

    public static final String METABOLIGHTS_COMPOUND_ID_REG_EXP = "(?:MTBLC|mtblc).+";
    public static final String COMPOUND_VAR = "compoundId";
    public static final String COMPOUND_MAPPING = "/{" + COMPOUND_VAR + ":" + METABOLIGHTS_COMPOUND_ID_REG_EXP + "}";



    //String PMCurl = "http://www.ebi.ac.uk/webservices/citexplore/v3.0.1/service?wsdl";
    private WSCitationImpl PMCSearchService;

    @RequestMapping(value = COMPOUND_MAPPING)
    @ResponseBody
    public Compound getCompound(@PathVariable(COMPOUND_VAR) String compoundId) throws DAOException {

		logger.info("requested compound " + compoundId);

        Compound compound = ModelObjectFactory.getCompound(compoundId);

        if (compound == null)
            throw new DAOException("The requested compound does not exist: "+ compoundId);

        return compound;

    }

    @RequestMapping(value = "/spectra/{spectraId}/json")
    public void getJsonSpectra(@PathVariable("spectraId") String spectraIdS, HttpServletResponse response) {


        // Convert the id to a long...
        long spectraId = Long.parseLong(spectraIdS);

        Spectra spectra = ModelObjectFactory.getSpectra(spectraId);

        FileUtil.streamFile(spectra.getPathToJsonSpectra(), response);


    }

    @RequestMapping(value = "/pathway/{pathwayId}/svg")
    public void getPathwayFilePng(@PathVariable("pathwayId") String pathwayIdS, HttpServletResponse response) {


        // Convert the id to a long...
        long pathwayId = Long.parseLong(pathwayIdS);

        Pathway pathway = ModelObjectFactory.getPathway(pathwayId);

        FileUtil.streamFile(pathway.getPathToPathwayFile(), response, "image/svg+xml");


    }

	@RequestMapping(value = "/pathway/{pathwayId}/png")
	public void getPathwayFileSvg(@PathVariable("pathwayId") String pathwayIdS, HttpServletResponse response) {


		// Convert the id to a long...
		long pathwayId = Long.parseLong(pathwayIdS);

		Pathway pathway = ModelObjectFactory.getPathway(pathwayId);

		FileUtil.streamFile(pathway.getPathToPathwayFile(), response, "image/png");


	}
    @RequestMapping(value = COMPOUND_MAPPING + "/reactions")
    @ResponseBody
    private List<Reaction> showReactions(@PathVariable(COMPOUND_VAR) String compound) {


        //Setting up resource client
        RheasResourceClient client = new RheasResourceClient();

        // Get the chebiid from the compound id
        String chebiId = ReferenceLayerImporter.MetaboLightsID2chebiID(compound);

		//Initialising and passing chebi Id as compound to Rhea
		List<Reaction> reactions = null;

		try {
			reactions = client.getRheasInCmlreact(chebiId);
		} catch (RheaFetchDataException e) {
			e.printStackTrace();
		}


        return reactions;
    }

    @RequestMapping(value = COMPOUND_MAPPING + "/citations")
    @ResponseBody
    private List<Result> showCitations(
            @PathVariable(COMPOUND_VAR) String compound) throws DAOException, MalformedURLException {


        // Get the chebiid from the compound id
        String chebiId = ReferenceLayerImporter.MetaboLightsID2chebiID(compound);

        PMCSearchService = new WSCitationImplService(new URL(PMCurl)).getWSCitationImplPort();

        //Initialising ResponseWrapper
        ResponseWrapper rslt = null;

        //Passing MTBLC compound id to Modelobjectfactory class
        Compound cmpd = ModelObjectFactory.getCompound(compound);
        if (cmpd == null)
            throw  new DAOException("The requested compound does not exist: "+ compound);

        //Creating a list object for DataItems
        List<DataItem> pmid = cmpd.getChebiEntity().getCitations();

        //Creating a list object for ResponseWrapper
        List<Result> rsltItems = new ArrayList<Result>();

        //Iterating the dataitems to get the citation object
        for (int x = 0; x < pmid.size(); x++) {
            String query = pmid.get(x).getData();
            String dataset = "metadata";
            String resultType = "core";
            int offSet = 0;
            String email = "";

            try {

                rslt = PMCSearchService.searchPublications(query, dataset, resultType, offSet, false, email);
                rsltItems.addAll(x, rslt.getResultList().getResult());
            } catch (QueryException_Exception e) {
                throw new DAOException("Couldn't get publications from Pubmed Central webservice. " , e);
            }
        }

        return rsltItems;
    }
}
