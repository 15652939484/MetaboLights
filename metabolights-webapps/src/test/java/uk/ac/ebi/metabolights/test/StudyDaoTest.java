package uk.ac.ebi.metabolights.test;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.ac.ebi.bioinvindex.model.Study;
import uk.ac.ebi.metabolights.service.StudyService;


public class StudyDaoTest extends TestCase {

	private ApplicationContext applicationContext;
	private StudyService studyService;

	public StudyService getStudyService() {
		return studyService;
	}


	public void setStudyService(StudyService studyService) {
		this.studyService = studyService;
	}


	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}


	public StudyDaoTest() {
		setApplicationContext(new ClassPathXmlApplicationContext("daotest.xml"));
		//setStudyService((StudyService) getApplicationContext().getBean("studyService"));	
	}
	
	public void testGetBiiStudy(){
		
		String studyAcc = "BII-S-1";

		try {
			
			Study study = getStudyService().getBiiStudy(studyAcc);

			if (study.getAcc().equals(studyAcc))
				assertTrue(true);
			
		}catch (Exception e) {
			assertTrue(false);
		}        

	}

}
