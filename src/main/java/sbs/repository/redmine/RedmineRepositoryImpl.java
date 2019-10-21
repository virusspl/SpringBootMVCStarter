package sbs.repository.redmine;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sbs.model.redmine.RedmineProject;
import sbs.model.redmine.RedmineUser;

@Repository
public class RedmineRepositoryImpl implements RedmineRepository {

	@Autowired
	@Qualifier("redmineJdbcTemplate")
	private JdbcTemplate jdbc;

	@Override
	public Map<Integer, RedmineProject> getPlProjects() {

		String query = 
				"SELECT "
				+ "projects.id, "
				+ "projects.name, "
				+ "projects.description "
				+ "FROM "
				+ "projects "
				+ "WHERE "
				+ "(projects.name LIKE ? OR projects.name LIKE ?) "
				+ "AND "
				+ "projects.status = 1";

		List<Map<String, Object>> resultSet = jdbc.queryForList(query, new Object[] {"%ATW%", "%WPS%"});
		
		Map<Integer, RedmineProject> result = new TreeMap<>();
		RedmineProject project;
		
		for (Map<String, Object> row : resultSet) {
			project = new RedmineProject();
			project.setId((int)row.get("id"));
			project.setName((String)row.get("name"));
			project.setDescription((String)row.get("description"));
			result.put(project.getId(), project);
		}
		
		return result;
	}

	
	private Map<Integer, RedmineUser> getPlUsersNoGroups() {

		String query = 
				"SELECT "
				+ "users.id, "
				+ "users.login, "
				+ "users.firstname, "
				+ "users.lastname, "
				+ "users.admin, "
				+ "email_addresses.address, "
				+ "users.created_on, "
				+ "users.updated_on, "
				+ "users.passwd_changed_on "
				+ "FROM "
				+ "((users LEFT JOIN email_addresses ON users.id = email_addresses.user_id) "				
				+ "INNER JOIN members ON users.id = members.user_id) "
				+ "INNER JOIN projects ON members.project_id = projects.id "
				+ "WHERE "
				+ "users.status = 1 "
				+ "AND "
				+ "users.type = ? "
				+ "AND "
				+ "(projects.name LIKE ? OR projects.name LIKE ?)"
				+ "GROUP BY users.id, users.login, users.firstname, users.lastname, users.admin, email_addresses.address, users.created_on, users.updated_on, users.passwd_changed_on";

		List<Map<String, Object>> resultSet = jdbc.queryForList(query, new Object[] {"User", "%ATW%", "%WPS%"});
		
		Map<Integer, RedmineUser> result = new TreeMap<>();
		RedmineUser user;
		
		for (Map<String, Object> row : resultSet) {
			user = new RedmineUser();
			
			user.setId((int)row.get("id"));
			user.setLogin((String)row.get("login"));
			user.setFirstName((String)row.get("firstname"));
			user.setLastName((String)row.get("lastname"));
			user.setAdmin((boolean)row.get("admin"));
			user.setMail((String)row.get("address"));
			user.setCreateDate((Timestamp)row.get("created_on"));
			user.setUpdateDate((Timestamp)row.get("updated_on"));
			user.setPassChangeDate((Timestamp)row.get("passwd_changed_on"));
			
			result.put(user.getId(), user);
		}
		return result;
	}
	
	@Override
	public Map<Integer, RedmineUser> getPlUsers() {
		
		Map<Integer, RedmineUser> users = getPlUsersNoGroups();
		Map<Integer, RedmineProject> projects = getPlProjects();

		String query = 
				"SELECT "
				+ "members.user_id, "
				+ "members.project_id "
				+ "FROM "
				+ "(users INNER JOIN members "
				+ "ON users.id = members.user_id) "
				+ "INNER JOIN projects "
				+ "ON members.project_id = projects.id "
				+ "WHERE "
				+ "projects.name LIKE ? OR projects.name LIKE ?";

		List<Map<String, Object>> resultSet = jdbc.queryForList(query, new Object[] {"%ATW%", "%WPS%"});
		
		int userId;
		int projectId;
		for (Map<String, Object> row : resultSet) {
			userId = (int)row.get("user_id");
			projectId = (int)row.get("project_id");
			
			if(!users.containsKey(userId) || !projects.containsKey(projectId)) {
				continue;
			}
			users.get(userId).getProjects().add(projects.get(projectId));
		}
		
		
		return users;
	}
	

}
