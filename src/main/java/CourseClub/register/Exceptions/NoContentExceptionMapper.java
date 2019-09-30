package CourseClub.register.Exceptions;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoContentExceptionMapper implements ExceptionMapper<NoContentException> {
    @Override
    public Response toResponse(NoContentException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 204);
        return Response.status(204)
                .entity(errorMessage)
                .build();
    }
}
