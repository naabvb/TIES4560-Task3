package CourseClub.register.Authentication;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class MySecurityContext implements SecurityContext {
	private User user;
	private String scheme;

	public MySecurityContext(User user, String scheme) {
		this.user = user;
		this.scheme = scheme;
	}

	@Override
	public Principal getUserPrincipal() {
		return this.user;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (user.getRole() != null) {
			return user.getRole().contains(role);
		}
		return false;
	}

	@Override
	public boolean isSecure() {
		return "https".contentEquals(this.scheme);
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

}
