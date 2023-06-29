package cardholder.exceptionhandler;

import cardholder.exception.CardHolderAlreadyExistsException;
import cardholder.exception.CardHolderNotFoundException;
import cardholder.exception.ClientNotCorrespondsException;
import cardholder.exception.CreditAnalysisNotFound;
import cardholder.exception.CreditCardNotFoundException;
import cardholder.exception.InvalidValueException;
import cardholder.exception.NegativeValueException;
import cardholder.exception.NoCreditAnalysisApprovedException;
import cardholder.exception.NoLimitAvailableException;
import cardholder.exception.NullValuesException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CardHolderExceptionHandler {
    public static final String TIMESTAMP = "timestamp";
    public static final String MDCKEY = "CorrelationId";
    private static final Logger LOOGER = LoggerFactory.getLogger(CardHolderExceptionHandler.class);

    @ExceptionHandler(NoCreditAnalysisApprovedException.class)
    public ProblemDetail noCreditAnalysisApprovedException(NoCreditAnalysisApprovedException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }


    @ExceptionHandler(InvalidValueException.class)
    public ProblemDetail invalidValueException(InvalidValueException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("http://example.com/invalid-value"));
        problemDetail.setTitle("Invalid value");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(CardHolderAlreadyExistsException.class)
    public ProblemDetail cardHolderAlreadyExistsException(CardHolderAlreadyExistsException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create("http://example.com/already-exists"));
        problemDetail.setTitle("Card Holder Already Exists");
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail illegalArgumentException(IllegalArgumentException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create("http://example.com/invalid-value"));
        problemDetail.setTitle("Invalid value");
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(CreditAnalysisNotFound.class)
    public ProblemDetail creditAnalysisNotFound(CreditAnalysisNotFound exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("http://example.com/not-found"));
        problemDetail.setTitle("Credit analysis not found");
        problemDetail.setStatus(HttpStatus.NOT_FOUND.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders/id"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(ClientNotCorrespondsException.class)
    public ProblemDetail clientNotCorrespondsException(ClientNotCorrespondsException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create("http://example.com/not-found"));
        problemDetail.setTitle("Client not corresponds");
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(NullValuesException.class)
    public ProblemDetail nullValuesException(NullValuesException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("http://example.com/bad-request"));
        problemDetail.setTitle("Null value");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(CardHolderNotFoundException.class)
    public ProblemDetail cardHolderNotFoundException(CardHolderNotFoundException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/424"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        return problemDetail;
    }

    @ExceptionHandler(NoLimitAvailableException.class)
    public ProblemDetail noLimitAvailableException(NoLimitAvailableException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create("http://example.com/already-exists"));
        problemDetail.setTitle("No limit available to card holder");
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders/id/cards"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(NegativeValueException.class)
    public ProblemDetail negativeValueException(NegativeValueException exception) {
        MDC.put(MDCKEY, UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("http://example.com/not-found"));
        problemDetail.setTitle("Negative value");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders/idCardHolder/cards/id"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(CreditCardNotFoundException.class)
    public ProblemDetail creditCardNotFoundException(CreditCardNotFoundException exception) {
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOOGER.error(String.valueOf(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("http://example.com/not-found"));
        problemDetail.setTitle("Credit analysis not found");
        problemDetail.setStatus(HttpStatus.NOT_FOUND.value());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create("/card-holders"));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        return problemDetail;
    }
}
