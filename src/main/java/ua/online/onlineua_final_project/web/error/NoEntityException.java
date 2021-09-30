package ua.online.onlineua_final_project.web.error;

public class NoEntityException extends RuntimeException {

    public NoEntityException(String message) {
        super(message);
    }

    public NoEntityException(String message, Throwable t) {
        super(message, t);
        this.setStackTrace(t.getStackTrace());
    }

    public NoEntityException(Throwable t) {
        super(t);
        this.setStackTrace(t.getStackTrace());
    }
}
