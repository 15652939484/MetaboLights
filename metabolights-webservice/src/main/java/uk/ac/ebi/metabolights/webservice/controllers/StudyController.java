/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 2015-Apr-22
 * Modified by:   kenneth
 *
 * Copyright 2015 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package uk.ac.ebi.metabolights.webservice.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.metabolights.repository.dao.DAOFactory;
import uk.ac.ebi.metabolights.repository.dao.StudyDAO;
import uk.ac.ebi.metabolights.repository.dao.filesystem.MzTabDAO;
import uk.ac.ebi.metabolights.repository.dao.filesystem.metabolightsuploader.IsaTabException;
import uk.ac.ebi.metabolights.repository.dao.hibernate.DAOException;
import uk.ac.ebi.metabolights.repository.model.*;
import uk.ac.ebi.metabolights.repository.model.webservice.RestResponse;
import uk.ac.ebi.metabolights.search.service.IndexingFailureException;
import uk.ac.ebi.metabolights.webservice.services.EmailService;

import java.io.File;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("study")
public class StudyController extends BasicController{

	@Autowired
	private EmailService emailService;

    public static final String METABOLIGHTS_ID_REG_EXP = "(?:MTBLS|mtbls).+";
	private final static Logger logger = LoggerFactory.getLogger(StudyController.class.getName());
	private StudyDAO studyDAO;

    @RequestMapping("{accession:" + METABOLIGHTS_ID_REG_EXP +"}")
	@ResponseBody
	public RestResponse<Study> getStudyById(@PathVariable("accession") String accession) throws DAOException {

		logger.info("Requesting " + accession + " to the webservice");

		return getStudy(accession, false, null);
	}

	@RequestMapping("obfuscationcode/{obfuscationcode}")
	@ResponseBody
	public RestResponse<Study> getStudyByObfuscationCode(@PathVariable("obfuscationcode") String obfuscationCode) throws DAOException {

		logger.info("Requesting study by obfuscation code " + obfuscationCode + " to the webservice");


		return getStudy(null, false, obfuscationCode);
	}


	@RequestMapping("{accession:" + METABOLIGHTS_ID_REG_EXP +"}/full")
	@ResponseBody
	public RestResponse<Study> getFullStudyById(@PathVariable("accession") String accession) throws DAOException {

		logger.info("Requesting full study " + accession + " to the webservice");

		return getStudy(accession, true, null);

	}

	@RequestMapping("list")
	@ResponseBody
	public RestResponse<String[]> getAllStudyAccessions() throws DAOException {

		logger.info("Requesting a list of all public studies from the webservice");

		RestResponse<String[]> response = new RestResponse<>();

		studyDAO = getStudyDAO();


		try {
			List<String> studyList = studyDAO.getList(getUser().getApiToken());

			String[] strarray = studyList.toArray(new String[0]);
			response.setContent(strarray);
		} catch (DAOException e) {
			logger.error("Can't get the list of studies", e);
			response.setMessage("Can't get the study requested.");
			response.setErr(e);
		}

		return response;

	}

	/**
	 * To update the public release date of a study.
	 * @param accession
	 * @param newPublicReleaseDate
	 * @return
	 */
	@RequestMapping(value = "{accession:" + METABOLIGHTS_ID_REG_EXP +"}/publicreleasedate", method= RequestMethod.PUT)
	@ResponseBody
	public RestResponse<Boolean> updatePublicReleaseDate(@PathVariable("accession") String accession, @RequestBody Date newPublicReleaseDate) throws Exception {

		User user = getUser();

		logger.info("User {} requested to update {} public release date to {}", user.getFullName(),accession, newPublicReleaseDate);

		studyDAO= getStudyDAO();

		// Update the public release date
		studyDAO.updateReleaseDate(accession, newPublicReleaseDate, user.getApiToken());

		// NOTE: Using IndexController as a Service..this could be refactored. We could have a Index service and a StudyService.
		// Like this we might have concurrency issues?
		Study study = studyDAO.getStudy(accession,user.getApiToken());

		IndexController.indexStudy (study);

		RestResponse<Boolean> restResponse = new RestResponse<>();
		restResponse.setContent(true);
		restResponse.setMessage("Public release date for " + accession + " updated to " + study.getStudyPublicReleaseDate() );

		logger.info("public release date updated.");


		// Email about this
		emailService.sendPublicReleaseDateUpdated(study);

		return restResponse;


	}

	/**
	 * To update the status of a study.
	 * @param accession
	 * @param newStatus
	 * @return
	 */
	@RequestMapping(value = "{accession:" + METABOLIGHTS_ID_REG_EXP +"}/status", method= RequestMethod.PUT)
	@ResponseBody
	public RestResponse<Boolean> updateStatus(@PathVariable("accession") String accession, @RequestBody LiteStudy.StudyStatus newStatus) throws DAOException, IsaTabException, IndexingFailureException {

		User user = getUser();

		logger.info("User {} requested to update {} status to {}", user.getFullName(),accession, newStatus.name());

		studyDAO= getStudyDAO();

		// Update the status
		studyDAO.updateStatus(accession, newStatus, user.getApiToken());

		// NOTE: Using IndexController as a Service..this could be refactored. We could have a Index service and a StudyService.
		// Like this we might have concurrency issues?
		Study study = studyDAO.getStudy(accession,user.getApiToken());

		IndexController.indexStudy (study);

		RestResponse<Boolean> restResponse = new RestResponse<>();
		restResponse.setContent(true);
		restResponse.setMessage("Status for " + accession + " updated to " + study.getStudyStatus() );

		logger.info("{} study status updated." , accession);

		// Email about this
		emailService.sendStatusChanged(study);

		return restResponse;


	}


	private RestResponse<Study> getStudy(String accession, boolean includeMAFFiles, String obfuscationCode) throws DAOException {

		RestResponse<Study> response = new RestResponse<Study>();

		studyDAO= getStudyDAO();

		// Get the study
		try {

			Study study = null;

			if (obfuscationCode == null) {
				study = studyDAO.getStudy(accession.toUpperCase(), getUser().getApiToken(), includeMAFFiles);
			} else {
				study = studyDAO.getStudyByObfuscationCode(obfuscationCode, includeMAFFiles);
			}

			response.setContent(study);

		} catch (DAOException e) {
			logger.error("Can't get the study requested " + accession, e);
			response.setMessage("Can't get the study requested.");
			response.setErr(e);
		}

		return  response;

	}

	private uk.ac.ebi.metabolights.repository.dao.StudyDAO getStudyDAO() throws DAOException {

		if (studyDAO == null){


			studyDAO = DAOFactory.getInstance().getStudyDAO();

		}
		return studyDAO;
	}

	@RequestMapping("{accession:" + METABOLIGHTS_ID_REG_EXP +"}/assay/{assayIndex}/maf")
	@ResponseBody
	public RestResponse<MetaboliteAssignment> getMetabolites(@PathVariable("accession") String accession, @PathVariable("assayIndex") String assayIndex) throws DAOException {


		logger.info("Requesting maf file of the assay " + assayIndex + " of the study " + accession + " to the webservice");

		// Get the study....
		// TODO: optimize this, since we are loading the whole study to get the MAF file name of one of the assay, and maf file can be loaded having only the maf
		RestResponse<Study> response = getStudy(accession, false, null);

		// Get the assay based on the index
		Assay assay = response.getContent().getAssays().get(Integer.parseInt(assayIndex)-1);

		MzTabDAO mzTabDAO = new MzTabDAO();
		MetaboliteAssignment metaboliteAssignment = new MetaboliteAssignment();


		String filePath = assay.getMetaboliteAssignment().getMetaboliteAssignmentFileName();

		if (filePath != null && !filePath.isEmpty()) {
			if (checkFileExists(filePath)) {
				logger.info("MAF file found, starting to read data from " + filePath);
				metaboliteAssignment = mzTabDAO.mapMetaboliteAssignmentFile(filePath);
			} else {
				logger.error("MAF file " + filePath + " does not exist!");
				metaboliteAssignment.setMetaboliteAssignmentFileName("ERROR: " + filePath + " does not exist!");
			}
		}

		return new RestResponse<MetaboliteAssignment>(metaboliteAssignment);
	}

	private boolean checkFileExists(String filePath){

		if (filePath == null || filePath.isEmpty())
			return false;        // No filename given

		File mafFile = new File(filePath);

		if (mafFile.exists())
			return true;

		return false;

	}
}
