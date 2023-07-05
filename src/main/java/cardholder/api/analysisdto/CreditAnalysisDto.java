package cardholder.api.analysisdto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysisDto(UUID id, Boolean approved, BigDecimal approvedLimit, BigDecimal withdrawalLimitValue, LocalDateTime date,
                                UUID clientId) {
    @Builder(toBuilder = true)
    public CreditAnalysisDto(UUID id, Boolean approved, BigDecimal approvedLimit, BigDecimal withdrawalLimitValue, LocalDateTime date,
                             UUID clientId) {
        this.id = id;
        this.approved = approved;
        this.approvedLimit = approvedLimit;
        this.withdrawalLimitValue = withdrawalLimitValue;
        this.date = date;
        this.clientId = clientId;
    }
}