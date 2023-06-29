package cardholder.model;

import cardholder.api.analysisdto.CreditAnalysisDto;
import cardholder.util.Status;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CardHolderModel(UUID clientId, BigDecimal creditLimit, BigDecimal withdrawalLimit, UUID creditAnalysisId, Status activeStatus,
                              BankAccountModel bankAccount) {
    @Builder(toBuilder = true)
    public CardHolderModel(UUID clientId, BigDecimal creditLimit, BigDecimal withdrawalLimit, UUID creditAnalysisId, Status activeStatus,
                           BankAccountModel bankAccount) {
        this.clientId = clientId;
        this.creditLimit = creditLimit;
        this.withdrawalLimit = withdrawalLimit;
        this.creditAnalysisId = creditAnalysisId;
        this.activeStatus = activeStatus == null ? Status.ACTIVE : activeStatus;
        this.bankAccount = bankAccount;
    }

    public CardHolderModel updateCardHolderFrom(CreditAnalysisDto creditAnalysis) {
        return this.toBuilder().creditLimit(creditAnalysis.approvedLimit()).withdrawalLimit(creditAnalysis.withdrawalLimitValue()).build();
    }
}