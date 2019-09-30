package CourseClub.register.Exceptions;

public class BadRequestException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public BadRequestException(String error) {
        super(error);
    }
}
