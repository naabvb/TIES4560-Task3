package CourseClub.register.Exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    public Response toResponse(BadRequestException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 404);
        return Response.status(400)
                .entity(errorMessage)
                .build();
    }
}
