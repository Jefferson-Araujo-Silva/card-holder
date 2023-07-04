package cardholder.controller.request;

import java.util.UUID;
import lombok.Builder;

public record CardHolderRequest(UUID clientId, UUID creditAnalysisId, BankAccountRequest bankAccount) {
    @Builder(toBuilder = true)
    public CardHolderRequest(UUID clientId, UUID creditAnalysisId, BankAccountRequest bankAccount) {
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.bankAccount = bankAccount;
    }

    public record BankAccountRequest(String account, String agency, String bankCode) {
        @Builder(toBuilder = true)
        public BankAccountRequest(String account, String agency, String bankCode) {
            this.account = account;
            this.agency = agency;
            this.bankCode = bankCode;
        }
    }
}