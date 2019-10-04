package CourseClub.register.Authentication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import CourseClub.register.Exceptions.ErrorMessage;
import CourseClub.register.Services.UserService;
import CourseClub.register.Types.User;

@Provider
public class SecurityFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
	private static final String SECURED_URL_PREFIX = "secured";

	private static final ErrorMessage FORBIDDEN_ErrMESSAGE = new ErrorMessage("Access blockedfor all users !!!", 403);
	private static final ErrorMessage UNAUTHORIZED_ErrMESSAGE = new ErrorMessage("User cannotaccess the resource.",
			401);
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		UserService UserService = new UserService();
		User user = null;

		Method resMethod = resourceInfo.getResourceMethod();
		Class<?> resClass = resourceInfo.getResourceClass();

		if (resMethod.isAnnotationPresent(PermitAll.class))
			return;
		if (resMethod.isAnnotationPresent(DenyAll.class)) {
			Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
			requestContext.abortWith(response);
		}

		if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
			if (rolesMatched(user, resMethod.getAnnotation(RolesAllowed.class)))
				return;
			Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE).build();
			requestContext.abortWith(response);
		}

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

			if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)
					|| (requestContext.getMethod().equals("DELETE"))) {
				if (user != null)
					return;

				ErrorMessage errorMessage = new ErrorMessage("User cannot access the resource.", 401);
				Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage)
						.build();
				requestContext.abortWith(unauthorizedStatus);
			}
		}

	}

	private boolean rolesMatched(User user, RolesAllowed annotation) {
		System.out.println(annotation);
		return false;
	}

}
