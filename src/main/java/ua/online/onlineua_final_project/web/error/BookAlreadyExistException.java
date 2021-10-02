package ua.online.onlineua_final_project.web.error;

public class BookAlreadyExistException extends RuntimeException {
    public BookAlreadyExistException() {
        super();
    }

    public BookAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BookAlreadyExistException(final String message) {
        super(message);
    }

    public BookAlreadyExistException(final Throwable cause) {
        super(cause);
    }
}
