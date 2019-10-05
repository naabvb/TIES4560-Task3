package CourseClub.register;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import CourseClub.register.Exceptions.ResourceNotFoundException;
import CourseClub.register.Services.UserService;
import CourseClub.register.Types.User;

@Singleton
@Path("/users")
public class UsersResource {

	static UserService usersService = new UserService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<User> getUser() throws NoContentException {
		List<User> users = usersService.getUsers();
		if (!users.isEmpty()) {
			return users;
		} else {
			throw new NoContentException("No users found."); // TODO FIX THIS (NoContentexception ei oo toteutettu)
		}
	}

	public static UserService getUserService() {
		return usersService;
	}

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public User getUser(@PathParam("userId") long id) {
		User user = usersService.getUserById(id);
		if (user == null) {
			throw new ResourceNotFoundException("User with id " + id + " not found.");
		}
		return user;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response createUser(User user, @Context UriInfo uriInfo) {
		User newUser = usersService.createUser(user);

		String newId = String.valueOf(newUser.getId());
		URI url = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(url).entity(newUser).build();
	}

}
