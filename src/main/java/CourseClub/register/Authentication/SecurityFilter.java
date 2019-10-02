package CourseClub.register.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import CourseClub.register.Exceptions.ErrorMessage;

@Provider
public class SecurityFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
	private static final String SECURED_URL_PREFIX = "secured";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		UserService UserService = new UserService();
		User user = null;

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

}
