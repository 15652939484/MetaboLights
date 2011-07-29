package uk.ac.ebi.metabolights.dao;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import uk.ac.ebi.bioinvindex.model.AssayResult;
import uk.ac.ebi.bioinvindex.model.Study;
import uk.ac.ebi.bioinvindex.model.VisibilityStatus;
import uk.ac.ebi.bioinvindex.model.security.User;
import uk.ac.ebi.metabolights.model.MetabolightsUser;


/**
 * DAO implementation for bioinvindex.model.Study.
 */
@Repository
public class StudyDAOImpl implements StudyDAO{

	private static Logger logger = Logger.getLogger(StudyDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Retrieve a study based on the accession identifier.  
	 */
	@Override
	public Study getStudy(String studyAcc) {

		Session session = sessionFactory.getCurrentSession();
		
		String queryStr = "FROM Study WHERE acc = :acc";
		
		Query q = session.createQuery(queryStr);
		q.setParameter("acc", studyAcc);	

		logger.debug("retrieving study "+studyAcc);
		Study study = (Study) q.uniqueResult();
	
		if (!study.getStatus().equals(VisibilityStatus.PUBLIC)){ //If not PUBLIC then must be owned by the user
			
			Boolean validUser = false;
			Long userId = new Long(0);
			
	 		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!auth.getPrincipal().equals(new String("anonymousUser"))){
				MetabolightsUser principal = (MetabolightsUser) auth.getPrincipal();
				userId = principal.getUserId();
			}
			
			if (userId>0){
				Collection<User> users = study.getUsers();
				Iterator<User> iter = users.iterator();
				while (iter.hasNext()){
					User user = (User) iter.next();
					if (user.getId().equals(userId)){
						validUser = true;
						break;
					}
				}
				
			}
			
			if (!validUser){
				Study invalidStudy = new Study();
				invalidStudy.setAcc("PRIVATE STUDY");
				invalidStudy.setDescription("This is a PRIVATE study, you are not Authorised to view this study.");
				invalidStudy.setTitle("Please log in as the submitter.");
				
				return invalidStudy;
				
			}
				
		}  // Study PUBLIC
		
		/*
		 * Initialize lazy collections here that we want to display .. otherwise the JSP will throw an error on rendering
		 */
		Hibernate.initialize(study.getContacts());
		Hibernate.initialize(study.getAnnotations());
		Hibernate.initialize(study.getPublications());
		//Hibernate.initialize(study.getUsers());

		Collection<AssayResult> assayResults = study.getAssayResults();
		Hibernate.initialize(assayResults);
		for (AssayResult assayResult : assayResults) {
			Hibernate.initialize(assayResult.getData().getFactorValues());
			Hibernate.initialize(assayResult.getCascadedPropertyValues());
			Hibernate.initialize(assayResult.getAssays());
		}
		Hibernate.initialize(study.getProtocols());
		
		session.clear();
		return study;
	}

}
