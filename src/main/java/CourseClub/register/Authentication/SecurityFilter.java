package CourseClub.register.Authentication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private static final String authMethod = "auth";
    private static final String realm = "courseclubregister.com";

    public String nonce;
    public ScheduledExecutorService nonceRefreshExecutor;

    private static final ErrorMessage FORBIDDEN_ErrMESSAGE = new ErrorMessage(
            "You have insufficient rights to the resource", 403);
    private static final ErrorMessage UNAUTHORIZED_ErrMESSAGE = new ErrorMessage(
            "Authorization required", 401);
    @Context
    private ResourceInfo resourceInfo;


    public SecurityFilter() throws Exception {

        nonce = calculateNonce();

        nonceRefreshExecutor = Executors.newScheduledThreadPool(1);

        nonceRefreshExecutor.scheduleAtFixedRate(new Runnable() {

            public void run() {
                nonce = calculateNonce();
            }
        }, 1, 1, TimeUnit.MINUTES);

    }


    /**
     * Calculates nonce
     * @return
     */
    public String calculateNonce() {
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
        String fmtDate = f.format(d);
        Random rand = new Random(100000);
        Integer randomInt = rand.nextInt();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((fmtDate + randomInt.toString()).getBytes());
            byte[] digest = md.digest();
            String nonceHash = DatatypeConverter.printHexBinary(digest)
                    .toUpperCase();
            return nonceHash;
        } catch (Exception e) {
            return "";
        }
    }


    private String getOpaque(String domain, String nonce) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((domain + nonce).getBytes());
            byte[] digest = md.digest();
            String opaqueHash = DatatypeConverter.printHexBinary(digest)
                    .toUpperCase();
            return opaqueHash;
        } catch (Exception e) {
            return "";
        }
    }


    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        // Returning without calling an abort function first means that
        // authorization
        // was successful.
        UserService userService = UsersResource.getUserService();
        Method resMethod = resourceInfo.getResourceMethod();
        User user = null;

        if (resMethod.isAnnotationPresent(PermitAll.class)) {
            return;
        }

        if (resMethod.isAnnotationPresent(DenyAll.class)) {
            abortWithForbidden(requestContext);
            return;
        }

        // TODO: Katso oikea dataflow Discordista.

        List<String> authHeader = requestContext.getHeaders()
                .get(AUTHORIZATION_HEADER_KEY);
        if (authHeader != null && authHeader.size() > 0) {
            if (authHeader.get(0)
                    .startsWith(AUTHORIZATION_HEADER_PREFIX_BASIC)) {
                String authToken = authHeader.get(0);
                authToken = authToken
                        .replaceFirst(AUTHORIZATION_HEADER_PREFIX_BASIC, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString,
                        ":");
                String username = tokenizer.nextToken();
                String password = tokenizer.nextToken();
                if (userService.userCredentialExists(username, password)) {
                    user = userService.getUser(username);
                    String scheme = requestContext.getUriInfo().getRequestUri()
                            .getScheme();
                    requestContext.setSecurityContext(
                            new MySecurityContext(user, scheme));
                }

            } else if (authHeader.get(0)
                    .startsWith(AUTHORIZATION_HEADER_PREFIX_DIGEST)) {
                abortWithUnauthorized(requestContext);
                return;
            }

            if (user == null) {
                abortWithUnauthorized(requestContext);
                return;
            }

            if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
                if (rolesMatched(user,
                        resMethod.getAnnotation(RolesAllowed.class))) {
                    return;
                }
                abortWithForbidden(requestContext);
                return;

            }
        }
        abortWithUnauthorized(requestContext);
        return;
    }

    private HashMap<String, String> parseAuthHeader(String header) {
        String headerContent = header
                .replaceFirst(AUTHORIZATION_HEADER_PREFIX_BASIC, "");
        
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
        if (!authMethod.isEmpty()) {
            header += "qop=" + authMethod + ",";
        }
        header += "nonce=\"" + nonce + "\",";
        header += "opaque=\"" + getOpaque(realm, nonce) + "\"";

        return header;
    }


    private void abortWithForbidden(ContainerRequestContext requestContext) {
        Response response = Response.status(Response.Status.FORBIDDEN)
                .entity(FORBIDDEN_ErrMESSAGE).build();
        requestContext.abortWith(response);
    }


    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        Response response = Response.status(Response.Status.UNAUTHORIZED)
                .entity(UNAUTHORIZED_ErrMESSAGE).header("WWW-Authenticate", getAuthenticateHeader())
                .build();

        requestContext.abortWith(response);
    }
}
