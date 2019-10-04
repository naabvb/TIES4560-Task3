package CourseClub.register.Authentication;

import java.io.IOException;
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
			"You have insufficient rights to the resource", 403);
	private static final ErrorMessage UNAUTHORIZED_ErrMESSAGE = new ErrorMessage("Authorization required", 401);
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Returning without calling an abort function first means that authorization was successful.
		UserService userService = UsersResource.getUserService();
		Method resMethod = resourceInfo.getResourceMethod();
		Class<?> resClass = resourceInfo.getResourceClass();
		
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		if (authHeader != null && authHeader.size() > 0) {
			String authToken = authHeader.get(0);
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
			String decodedString = Base64.decodeAsString(authToken);
			StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
			String username = tokenizer.nextToken();
			String password = tokenizer.nextToken();
			if (!userService.userCredentialExists(username, password)) {
				abortWithUnauthorized(requestContext);
				return;
			}
			User user = userService.getUser(username);
			String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
			requestContext.setSecurityContext(new MySecurityContext(user, scheme));
			
			if (resClass.isAnnotationPresent(PermitAll.class)
					|| resMethod.isAnnotationPresent(PermitAll.class)) {
				return;
			}
			
			if (resClass.isAnnotationPresent(DenyAll.class)
					|| resMethod.isAnnotationPresent(DenyAll.class)) {
				abortWithForbidden(requestContext);
				return;
			}

			if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
				if (rolesMatched(user, resMethod.getAnnotation(RolesAllowed.class))) {
					return;
				}
				abortWithForbidden(requestContext);
				return;
			} else if (resClass.isAnnotationPresent(RolesAllowed.class)) {
				if (rolesMatched(user, resClass.getAnnotation(RolesAllowed.class))) {
					return;
				}
				abortWithForbidden(requestContext);
				return;
			}
		}
		abortWithUnauthorized(requestContext);
		return;
	}

	private boolean rolesMatched(User user, RolesAllowed annotation) {
		List<String> roles = user.getRole();
		String annotationStr = annotation.toString();
		for (String role : roles) {
			if (annotationStr.contains(role)) {
				return true;
			}
		}
		return false;
	}
	
	private void abortWithForbidden(ContainerRequestContext requestContext) {
		Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
		requestContext.abortWith(response);
	}
	
	private void abortWithUnauthorized(ContainerRequestContext requestContext) {
		Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE).build();
		requestContext.abortWith(response);
	}
}
