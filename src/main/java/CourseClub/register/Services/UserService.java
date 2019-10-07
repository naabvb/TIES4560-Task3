package CourseClub.register.Services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import CourseClub.register.Exceptions.BadRequestException;
import CourseClub.register.Types.User;

@Singleton
public class UserService {

	private List<String> nonces;
	private List<User> users;
	private static long nextId = 0L;

	public UserService() {
		this.users = new ArrayList<User>();
		this.nonces = new ArrayList<String>();
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
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getLogin().equals(username) && users.get(i).getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	public User getUser(String username) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getLogin().equals(username)) {
				return users.get(i);
			}
		}
		return null;
	}

	public User createUser(User user) {
		if (user.hasRequiredAttributes()) {
			user.setId(nextId);
			user.addRole("admin");
			nextId++;
			users.add(user);
			return user;
		} else {
			throw new BadRequestException("Couldn't add user; missing required attributes.");
		}
	}

	public User getUserById(long id) {
		int index = findUserIndex(id);
		if (index >= 0) {
			return users.get(index);
		}
		return null;
	}

	private int findUserIndex(long id) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getId() == id)
				return i;
		}

		return -1;
	}
	
	public List<String> getNonces() {
		return nonces;
	}
	
	public void addNonce(String nonce) {
		nonces.add(nonce);
	}
	
	public void deleteNonce(String nonce) {
		nonces.remove(nonce);
	}

}
