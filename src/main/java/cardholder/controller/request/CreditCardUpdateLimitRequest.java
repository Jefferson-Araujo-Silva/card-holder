package cardholder.controller.request;

import java.math.BigDecimal;
import lombok.Builder;

public record CreditCardUpdateLimitRequest(BigDecimal limit) {
    @Builder(toBuilder = true)
    public CreditCardUpdateLimitRequest(BigDecimal limit) {
        this.limit = limit;
    }
}
