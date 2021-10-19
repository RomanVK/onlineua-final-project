package ua.online.onlineua_final_project.web.error;

public class UserAlreadyHasTheBook extends RuntimeException {

    public UserAlreadyHasTheBook() {
        super();
    }

    public UserAlreadyHasTheBook(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyHasTheBook(final String message) {
        super(message);
    }

    public UserAlreadyHasTheBook(final Throwable cause) {
        super(cause);
    }

}