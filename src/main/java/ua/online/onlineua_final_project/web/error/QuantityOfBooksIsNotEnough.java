package ua.online.onlineua_final_project.web.error;

public class QuantityOfBooksIsNotEnough extends RuntimeException {

    public QuantityOfBooksIsNotEnough() {
        super();
    }

    public QuantityOfBooksIsNotEnough(final String message, final Throwable cause) {
        super(message, cause);
    }

    public QuantityOfBooksIsNotEnough(final String message) {
        super(message);
    }

    public QuantityOfBooksIsNotEnough(final Throwable cause) {
        super(cause);
    }

}