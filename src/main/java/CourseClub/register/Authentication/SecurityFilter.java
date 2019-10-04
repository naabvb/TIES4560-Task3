package CourseClub.register.Authentication;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import CourseClub.register.UsersResource;
import CourseClub.register.Exceptions.ErrorMessage;
import CourseClub.register.Services.UserService;
import CourseClub.register.Types.User;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

	private static final ErrorMessage FORBIDDEN_ErrMESSAGE = new ErrorMessage(
			"You have insufficient right to the resource", 403);
	private static final ErrorMessage UNAUTHORIZED_ErrMESSAGE = new ErrorMessage("Authorization required", 401);
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		UserService UserService = UsersResource.getUserService();
		User user = null;

		Method resMethod = resourceInfo.getResourceMethod();
		Class<?> resClass = resourceInfo.getResourceClass(); // TODO Käytännössä nyt tarkistetaan vain metodien, mutta
																// toisaalta varmaan tarvitaan vaan metodeilla???
		
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		if (authHeader != null && authHeader.size() > 0) {
			String authToken = authHeader.get(0);
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
			String decodedString = Base64.decodeAsString(authToken);
			StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
			String username = tokenizer.nextToken();
			String password = tokenizer.nextToken();
			if (UserService.userCredentialExists(username, password)) {
				user = UserService.getUser(username);
				String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
				requestContext.setSecurityContext(new MySecurityContext(user, scheme));
			}
			
			// TODO: Pitäisikö tämän olla jo riittävä ehto yksinään, ilman authHeaderia?
			// if (resMethod.isAnnotationPresent(PermitAll.class)) {
			if (resClass.isAnnotationPresent(PermitAll.class)
					|| resMethod.isAnnotationPresent(PermitAll.class)) {
				return;
			}
				
			// TODO: Jos kukaan ei saa käyttää resurssia, tarvitaanko tässäkään authHeaderia ja tunnuksia?
			// if (resMethod.isAnnotationPresent(DenyAll.class)) {
			if (resClass.isAnnotationPresent(DenyAll.class)
					|| resMethod.isAnnotationPresent(PermitAll.class)) {
				Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
				requestContext.abortWith(response);
			}

			if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
				if (areRolesMatched(user, resMethod.getAnnotation(RolesAllowed.class))) {
					return;
				}
				Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
				requestContext.abortWith(response);
			}
		}
		Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE).build();
		requestContext.abortWith(response);
	}

	private boolean areRolesMatched(User user, RolesAllowed annotation) {
		List<String> roles = user.getRole();
		for (int i = 0; i < roles.size(); i++) {
			if (annotation.toString().contains(roles.get(i))) {
				return true;
			}
		}

		return false;
	}

}
