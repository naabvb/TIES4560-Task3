package CourseClub.register;

import javax.inject.Singleton;
import javax.ws.rs.Path;

import CourseClub.register.Services.UserService;

@Singleton
@Path("/users")
public class UsersResource {

	UserService usersService = new UserService();

}
