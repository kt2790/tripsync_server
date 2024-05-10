package kt.tripsync.exception;

public class DuplicateBookmarkException extends RuntimeException {
    public DuplicateBookmarkException() {
    }

    public DuplicateBookmarkException(String message) {
        super(message);
    }

    public DuplicateBookmarkException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateBookmarkException(Throwable cause) {
        super(cause);
    }
}
