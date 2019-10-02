package CourseClub.register.Types;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class User implements Principal {
	private String firstName;
	private String lastName;
	private long id;
	private String login;
	private String password;

	private List<String> role;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User() {
		// stub
	}

	public User(String firstName, String lastName, String login, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
		this.role = new ArrayList<String>();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public boolean hasRequiredAttributes() {
		// TODO PLEASE DOERINO THE COPYPASTERINO
		return true;
	}

}
