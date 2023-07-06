package cardholder.exception;

public class NoLimitAvailableException extends RuntimeException {
    public NoLimitAvailableException(String message) {
        super(message);
    }
}
