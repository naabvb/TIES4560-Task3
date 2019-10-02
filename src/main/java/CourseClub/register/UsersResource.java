package CourseClub.register;

import java.net.URI;

import javax.inject.Singleton;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import CourseClub.register.Services.UserService;
import CourseClub.register.Types.User;

@Singleton
@Path("/users")
public class UsersResource {

	UserService usersService = new UserService();

	public Response createUser(User user, @Context UriInfo uriInfo) {
		User newUser = usersService.createUser(user);

		String newId = String.valueOf(newUser.getId());
		URI url = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(url).entity(newUser).build();
	}

}
