package haivo.us.crypto.mechanoid.net;

public class ServiceException extends Exception {
    private static final long serialVersionUID = 1;

    public ServiceException(Throwable cause) {
        initCause(cause);
    }

    public String getMessage() {
        return getCause().getMessage();
    }
}
