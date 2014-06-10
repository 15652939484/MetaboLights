package uk.ac.ebi.metabolights.webservice.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.metabolights.webservice.dao.SecurityDAO;

import javax.sql.DataSource;

/**
 * User: conesa
 * Date: 04/06/2014
 * Time: 15:11
 */
@Repository
public class JdbcSecurityDAO  implements SecurityDAO {

	private JdbcTemplate jdbcTemplate;
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public boolean canUserAccessStudy(String userToken, String studyId) {

		String sql = "select acc from (" +
						"select s.acc, 'public' as api_token " +
						"from study s " +
						"where s.status = 0 " +
						"union " +
						"select s.acc, u.api_token " +
						"from user_detail u, study2user su, study s " +
						"where " +
						"u.id = su.user_id and su.study_id = s.id and s.status = 1 " +
						"union " +
						"select s.acc, u.api_token " +
						"from " +
						"user_detail u, study s " +
						"where " +
						"s.status = 1 and u.role = 1)" +
						"where api_token in(?,'public') and acc=?";
//				--api_token in('2e2ea571-9a49-4640-9fc8-d096e2130770','public') --sneumann
//		api_token in('ad3520eb-4d1a-4030-8eef-a42381804789','public') --kenneth
//		http://docs.spring.io/spring/docs/3.1.0.RELEASE/reference/html/jdbc.html

		String acc = this.jdbcTemplate.queryForObject(sql, String.class, new Object[]{userToken, studyId});


		return (acc != null);
	}
}
