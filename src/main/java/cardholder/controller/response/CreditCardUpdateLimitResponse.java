package cardholder.controller.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CreditCardUpdateLimitResponse(UUID cardId, BigDecimal updatedLimit) {
    @Builder(toBuilder = true)
    public CreditCardUpdateLimitResponse(UUID cardId, BigDecimal updatedLimit) {
        this.cardId = cardId;
        this.updatedLimit = updatedLimit;
    }
}
