package kt.tripsync.exception;

public class FriendAlreadyExistsException extends RuntimeException {
    public FriendAlreadyExistsException() {
        super();
    }

    public FriendAlreadyExistsException(String message) {
        super(message);
    }

    public FriendAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FriendAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected FriendAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
