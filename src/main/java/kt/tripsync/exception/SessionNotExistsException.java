package kt.tripsync.exception;

public class SessionNotExistsException extends RuntimeException {

    public SessionNotExistsException() {
        super();
    }

    public SessionNotExistsException(String message) {
        super(message);
    }

    public SessionNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionNotExistsException(Throwable cause) {
        super(cause);
    }
}
