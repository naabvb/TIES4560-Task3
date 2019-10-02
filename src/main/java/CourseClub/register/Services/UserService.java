package CourseClub.register.Services;

import java.util.ArrayList;
import java.util.List;

import CourseClub.register.Exceptions.BadRequestException;
import CourseClub.register.Types.User;

public class UserService {

	private List<User> users;
	private static long nextId = 0L;

	public UserService() {
		this.users = new ArrayList<User>();
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public static long getNextId() {
		return nextId;
	}

	public static void setNextId(long nextId) {
		UserService.nextId = nextId;
	}

	public boolean userCredentialExists(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	public User getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public User createUser(User user) {
		if (user.hasRequiredAttributes()) {
			user.setId(nextId);
			nextId++;
			users.add(user);
			return user;
		} else {
			throw new BadRequestException("Couldn't add user; missing required attributes.");
		}
	}

}
