package CourseClub.register.Authentication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
import javax.xml.bind.DatatypeConverter;

import org.glassfish.jersey.internal.util.Base64;

import CourseClub.register.UsersResource;
import CourseClub.register.Exceptions.ErrorMessage;
import CourseClub.register.Services.UserService;
import CourseClub.register.Types.User;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX_BASIC = "Basic ";
	private static final String AUTHORIZATION_HEADER_PREFIX_DIGEST = "Digest ";

	private static final String realm = "courseclubregister.com";

	public String nonce;

	private static final ErrorMessage FORBIDDEN_ErrMESSAGE = new ErrorMessage(
			"You have insufficient rights to the resource", 403);
	private static final ErrorMessage UNAUTHORIZED_ErrMESSAGE = new ErrorMessage("Authorization required", 401);
	@Context
	private ResourceInfo resourceInfo;

	private String calculateNonce() {
		Date d = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
		String fmtDate = f.format(d);
		Random rand = new Random(100000);
		Integer randomInt = rand.nextInt();
		return md5hex(fmtDate + randomInt.toString());
	}

	private String getOpaque(String domain, String nonce) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update((domain + nonce).getBytes());
			byte[] digest = md.digest();
			String opaqueHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
			return opaqueHash;
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Returning without calling an abort function first means that
		// authorization was successful.

		UserService userService = UsersResource.getUserService();
		nonce = calculateNonce();
		userService.addNonce(nonce);
		Method resMethod = resourceInfo.getResourceMethod();
		User user = null;

		if (resMethod.isAnnotationPresent(PermitAll.class)) {
			return;
		}

		if (resMethod.isAnnotationPresent(DenyAll.class)) {
			abortWithForbidden(requestContext);
			return;
		}

		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		if (authHeader != null && authHeader.size() > 0) {
			if (authHeader.get(0).startsWith(AUTHORIZATION_HEADER_PREFIX_BASIC)) {

				user = basicAuth(authHeader, userService, requestContext);

			} else if (authHeader.get(0).startsWith(AUTHORIZATION_HEADER_PREFIX_DIGEST)) {

				user = digestAuth(authHeader, userService, requestContext);
				if (user == null)
					return;

			}

			if (user == null) {
				abortWithUnauthorized(requestContext);
				return;
			}

			if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
				if (rolesMatched(user, resMethod.getAnnotation(RolesAllowed.class))) {
					return;
				}
				abortWithForbidden(requestContext);
				return;

			}
		}
		abortWithUnauthorized(requestContext);
		return;
	}

	private User basicAuth(List<String> authHeader, UserService userService, ContainerRequestContext requestContext) {
		User user = null;
		String authToken = authHeader.get(0);
		authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX_BASIC, "");
		String decodedString = Base64.decodeAsString(authToken);
		StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
		String username = tokenizer.nextToken();
		String password = tokenizer.nextToken();
		if (userService.userCredentialExists(username, password)) {
			user = userService.getUser(username);
			String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
			requestContext.setSecurityContext(new MySecurityContext(user, scheme));
		}
		return user;
	}

	private User digestAuth(List<String> authHeader, UserService userService, ContainerRequestContext requestContext) {
		HashMap<String, String> headerValues = parseAuthHeader(authHeader.get(0));

		String userNonce = headerValues.get("nonce");

		if (!userService.getNonces().contains(userNonce)) {
			abortWithUnauthorizedStale(requestContext);
			return null;
		}

		String method = requestContext.getMethod();

		String reqURI = headerValues.get("uri");

		String username = headerValues.get("username");
		User user = userService.getUser(username);
		if (user == null) {
			userService.deleteNonce(userNonce);
			abortWithUnauthorized(requestContext);
			return null;
		}

		String ha1 = md5hex(user.getLogin() + ":" + realm + ":" + user.getPassword());
		String ha2 = md5hex(method + ":" + reqURI);
		String serverResponse = md5hex(ha1 + ":" + userNonce + ":" + ha2);

		String clientResponse = headerValues.get("response");

		if (serverResponse.equals(clientResponse)) {
			String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
			requestContext.setSecurityContext(new MySecurityContext(user, scheme));
			userService.deleteNonce(userNonce);
			return user;
		}
		abortWithUnauthorized(requestContext);
		return null;
	}

	private String md5hex(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte[] digest = md.digest();
			return DatatypeConverter.printHexBinary(digest).toLowerCase();
		} catch (Exception e) {
			return "";
		}
	}

	private HashMap<String, String> parseAuthHeader(String header) {
		String headerContent = header.replaceFirst(AUTHORIZATION_HEADER_PREFIX_DIGEST, "");
		HashMap<String, String> headerValues = new HashMap<String, String>();
		String[] valueArray = headerContent.split(",");
		for (String keyVal : valueArray) {
			if (keyVal.contains("=")) {
				String key = keyVal.substring(0, keyVal.indexOf("="));
				String value = keyVal.substring(keyVal.indexOf("=") + 1);
				headerValues.put(key.trim(), value.replaceAll("\"", "").trim());
			}
		}
		return headerValues;
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

	private String getAuthenticateHeader() {
		String header = "";

		header += "Digest realm=\"" + realm + "\",";
		header += "nonce=\"" + nonce + "\",";
		header += "opaque=\"" + getOpaque(realm, nonce) + "\"";

		return header;
	}

	private void abortWithForbidden(ContainerRequestContext requestContext) {
		Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE).build();
		requestContext.abortWith(response);
	}

	private void abortWithUnauthorized(ContainerRequestContext requestContext) {

		Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE)
				.header("WWW-Authenticate", getAuthenticateHeader()).build();

		requestContext.abortWith(response);
	}

	private void abortWithUnauthorizedStale(ContainerRequestContext requestContext) {

		Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE)
				.header("WWW-Authenticate", getAuthenticateHeader()).header("stale", "true").build();

		requestContext.abortWith(response);
	}
}
