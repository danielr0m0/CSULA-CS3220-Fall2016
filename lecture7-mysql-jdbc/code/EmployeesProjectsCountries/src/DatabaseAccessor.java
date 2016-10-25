import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessor {

	public static final String LIST_COUNTRIES_QUERY = "select * from countries";
	

	//Selecting both employees and their country at the same time. That way we only need to do one query.
	public static final String LIST_EMPLOYEES_COUNTRIES_QUERY = "select e.id as 'employee_id', e.first_name, e.last_name, e.address, c.id as 'country_id', c.name as 'country_name' from employees e, countries c where e.country_id = c.id";

	//Retrieve project and leader info
	public static final String LIST_PROJECTS_QUERY =  "select p.id as 'project_id', p.name as 'project_name', p.leader_id as 'leader_id', e.first_name as 'first_name', e.last_name as 'last_name', e.address as 'address', c.id as 'country_id', c.name as 'country_name' from projects p, employees e, countries c where p.leader_id = e.id and e.country_id = c.id";

	//Retrieve member info and ratings for specific project
	public static final String LIST_PROJECTS_MEMBERS_QUERY = "select e.id as 'employee_id', e.first_name, e.last_name, e.address, c.id as 'country_id', c.name as 'country_name' from employees e, countries c where e.country_id = c.id and ";

	
	
	public static List<Project> getProjects() throws SQLException {
		List<Project> projects = new ArrayList<>();
		Connection c = null;
		try {

			c = ConnectionUtils.getMySQLConnection(DatabaseConfig.MYSQL_USERNAME, DatabaseConfig.MYSQL_PASSWORD,
					DatabaseConfig.MYSQL_HOST, DatabaseConfig.MYSQL_PORT, DatabaseConfig.MYSQL_DATABASE_TO_USE);

			PreparedStatement stmt = c.prepareStatement(LIST_PROJECTS_QUERY);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int projectId = rs.getInt("project_id");
				String projectName = rs.getString("project_name");
				int leaderId = rs.getInt("leader_id");
				String leaderFirstName = rs.getString("first_name");
				String leaderLastName = rs.getString("last_name");
				String address = rs.getString("address");
				int countryId = rs.getInt("country_id");
				String countryName = rs.getString("country_name");
				
				Country country = new Country(countryId, countryName);
				Employee leader = new Employee(leaderId, leaderFirstName, leaderLastName, address, country);
				Project project = new Project(projectId, projectName, leader, null);
				projects.add(project);
			}

		} catch (SQLException e) {
			// Escalate to Server error
			throw e;
		}
		// Always close connections, no matter what happened
		finally {
			try {
				if (c != null)
					c.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		
		return projects;
	}
	
	public static List<Country> getCountries() throws SQLException {
		Connection c = null;
		List<Country> countries = new ArrayList<>();
		try {

			c = ConnectionUtils.getMySQLConnection(DatabaseConfig.MYSQL_USERNAME, DatabaseConfig.MYSQL_PASSWORD,
					DatabaseConfig.MYSQL_HOST, DatabaseConfig.MYSQL_PORT, DatabaseConfig.MYSQL_DATABASE_TO_USE);

			PreparedStatement stmt = c.prepareStatement(LIST_COUNTRIES_QUERY);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				countries.add(new Country(id, name));
			}

		} catch (SQLException e) {
			// Escalate to Server error
			throw e;
		}
		// Always close connections, no matter what happened
		finally {
			try {
				if (c != null)
					c.close();
			} catch (SQLException e) {
				throw e;
			}
		}

		return countries;
		
	}
	
	public static List<Employee> getEmployees() throws SQLException {
		Connection c = null;
		List<Employee> employees = new ArrayList<>();
		try {

			c = ConnectionUtils.getMySQLConnection(DatabaseConfig.MYSQL_USERNAME, DatabaseConfig.MYSQL_PASSWORD,
					DatabaseConfig.MYSQL_HOST, DatabaseConfig.MYSQL_PORT, DatabaseConfig.MYSQL_DATABASE_TO_USE);

			PreparedStatement stmt = c.prepareStatement(LIST_EMPLOYEES_COUNTRIES_QUERY);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("employee_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String address = rs.getString("address");

				int countryId = rs.getInt("country_id");
				String countryName = rs.getString("country_name");
				employees.add(new Employee(id, firstName, lastName, address, new Country(countryId, countryName)));
			}

		} catch (SQLException e) {
			// Escalate to Server error
			throw e;
		}
		// Always close connections, no matter what happened
		finally {
			try {
				if (c != null)
					c.close();
			} catch (SQLException e) {
				throw e;
			}
		}

		return employees;

	}

}