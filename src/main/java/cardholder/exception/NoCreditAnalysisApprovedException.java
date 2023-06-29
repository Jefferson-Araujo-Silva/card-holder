package cardholder.exception;

public class NoCreditAnalysisApprovedException extends RuntimeException {
    public NoCreditAnalysisApprovedException(String message) {
        super(message);
    }
}
