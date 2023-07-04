package cardholder.exception;

public class CardHolderAlreadyExistsException extends RuntimeException {

    public CardHolderAlreadyExistsException(String message) {
        super(message);
    }
}
