/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 * Last modified: 6/10/14 11:57 AM
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

package uk.ac.ebi.metabolights.repository.dao.filesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.metabolights.repository.dao.hibernate.DAOException;
import uk.ac.ebi.metabolights.repository.model.Study;
import uk.ac.ebi.metabolights.repository.utils.IsaTab2MetaboLightsConverter;

import java.io.File;
import java.io.FilenameFilter;

/**
 * User: conesa
 * Date: 28/08/2013
 * Time: 14:41
 */
public class StudyDAO {

    private IsaTabInvestigationDAO isaTabInvestigationDAO;
    private File publicFolder;
    private File privateFolder;
    private final static Logger logger = LoggerFactory.getLogger(StudyDAO.class.getName());

    public StudyDAO(String isaTabRootConfigurationFolder, String publicFolder, String privateFolder){
        this.isaTabInvestigationDAO = new IsaTabInvestigationDAO(isaTabRootConfigurationFolder);
        this.publicFolder = new File(publicFolder);
        this.privateFolder = new File(privateFolder);

    }

    public Study getStudy(String accession, boolean includeMetabolites) throws DAOException {

        Study newStudy = new Study();
        newStudy.setStudyIdentifier(accession);
        return fillStudy(includeMetabolites, newStudy);

    }

    private File getInvestigationFolder(final String metabolightsId, File location){

        logger.info("Study location is "+location+" for study "+metabolightsId);

        File[] files = location.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                return s.equals(metabolightsId);
            }
        });

        if (files != null && files.length == 1 ){
            return  files[0];
        } else {
            return null;
        }


    }
	public boolean isStudyPublic(String metaboLightsId){
		return (getInvestigationFolder(metaboLightsId,publicFolder) != null);
	}

    public Study fillStudy( boolean includeMetabolites, Study studyToFill) throws DAOException {

        logger.info("Trying to parse study "+ studyToFill.getStudyIdentifier());

        // Try from the expected folder
        File studyFolder = getInvestigationFolder(studyToFill.getStudyIdentifier(), studyToFill.isPublicStudy()?publicFolder:privateFolder);

        // If we got nothing...
        if (studyFolder == null) {

            // Try other studies location but there is a discrepancy between the DB and the Filesystem
            studyFolder = getInvestigationFolder(studyToFill.getStudyIdentifier(), studyToFill.isPublicStudy()?privateFolder:publicFolder);

            // Warn about this:
            if (studyFolder != null) {
                logger.warn("Misplaced folder for the study " + studyToFill.getStudyIdentifier() + ": found here " + studyFolder.getAbsolutePath() + " and public=" + studyToFill.isPublicStudy());
            }


        }

        // We got something ...
        if (studyFolder != null){

            // Load the IsaTab investigation
            org.isatools.isacreator.model.Investigation isaInvestigation = isaTabInvestigationDAO.getInvestigation(studyFolder.getAbsolutePath());

            // Convert it into a MetaboLights study
             studyToFill = IsaTab2MetaboLightsConverter.convert(isaInvestigation, studyFolder.getAbsolutePath(), includeMetabolites, studyToFill);


            studyToFill.setStudyLocation(studyFolder.getAbsolutePath());

            logger.info("Loaded study "+ studyToFill.getStudyIdentifier());

            return studyToFill;

        } else {
            throw new DAOException("Study folder for " + studyToFill.getStudyIdentifier() + " not found." );
        }



    }
}
