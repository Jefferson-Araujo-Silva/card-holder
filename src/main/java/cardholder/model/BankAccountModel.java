package cardholder.model;

import cardholder.exception.InvalidValueException;
import cardholder.exception.NullValuesException;
import lombok.Builder;

public record BankAccountModel(String account, String agency, String bankCode) {
    @Builder(toBuilder = true)
    public BankAccountModel {
        if (account == null || agency == null || bankCode == null) {
            throw new NullValuesException("Any value in bank account is null");
        }
        String exceptionMessage = !bankCode.matches("\\d{3}") ? "Invalid bank code number" : null;
        exceptionMessage = !agency.matches("\\d{4}") ? "Invalid agency number" : exceptionMessage;
        exceptionMessage = !account.matches("\\d{8}-\\d") ? "Invalid account number" : exceptionMessage;
        if (exceptionMessage != null) {
            throw new InvalidValueException(exceptionMessage);
        }
    }

    @Override
    public String account() {
        return account;
    }

    @Override
    public String agency() {
        return agency;
    }

    @Override
    public String bankCode() {
        return bankCode;
    }
}
