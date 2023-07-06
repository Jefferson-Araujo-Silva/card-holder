package cardholder.controller.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

public record CardHolderResponse(UUID cardHolderId, String status,

                                 BigDecimal limit, LocalDateTime createdAt) {
    @Builder(toBuilder = true)
    public CardHolderResponse(UUID cardHolderId, String status, BigDecimal limit, LocalDateTime createdAt) {
        this.cardHolderId = cardHolderId;
        this.status = status;
        this.limit = limit;
        this.createdAt = createdAt;
    }
}
