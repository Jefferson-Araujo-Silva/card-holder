package cardholder.controller.request;

import java.math.BigDecimal;
import lombok.Builder;

public record CreditCardRequest(BigDecimal limit) {
    @Builder(toBuilder = true)
    public CreditCardRequest(BigDecimal limit) {
        this.limit = limit;
    }
}
