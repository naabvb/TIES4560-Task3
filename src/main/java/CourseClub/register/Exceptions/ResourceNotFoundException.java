package CourseClub.register.Exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String error) {
        super(error);
    }
}
