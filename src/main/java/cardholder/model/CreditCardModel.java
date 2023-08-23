package cardholder.model;

import cardholder.exception.NegativeValueException;
import cardholder.repository.entity.CardHolderEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

public record CreditCardModel(CardHolderEntity cardHolder, BigDecimal limit, BigDecimal withdrawalLimit, String cardNumber, String cvv,
                              LocalDate dueDate) {
    @Builder(toBuilder = true)
    public CreditCardModel(CardHolderEntity cardHolder, BigDecimal limit, BigDecimal withdrawalLimit, String cardNumber, String cvv,
                           LocalDate dueDate) {
        this.cardHolder = cardHolder;
        if (limit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeValueException("Limit cannot be less than or equal to zero");
        }
        this.limit = limit;
        this.withdrawalLimit = withdrawalLimit;
        this.cardNumber = cardNumber == null ? generateCardNumber() : cardNumber;
        this.cvv = cvv == null ? generateCvv() : cvv;
        this.dueDate = dueDate == null ? generateDueDate() : dueDate;
    }

    public static String generateCvv() {
        final StringBuilder cvv = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cvv.append((int) (Math.random() * 10));
        }
        return cvv.toString();
    }

    public static LocalDate generateDueDate() {
        return LocalDate.now().plusYears(5);
    }

    public static String generateCardNumber() {
        final StringBuilder cardNumber = new StringBuilder();
        cardNumber.append("4");
        for (int i = 0; i < 15; i++) {
            cardNumber.append((int) (Math.random() * 10));
        }
        return cardNumber.toString();
    }

    public CreditCardModel updateCreditCardModel(CreditCardModel model, CardHolderEntity cardHolder) {
        return this.toBuilder().cardHolder(cardHolder).limit(model.limit).withdrawalLimit(cardHolder.getWithdrawalLimit())
                .cardNumber(model.cardNumber).cvv(model.cvv).dueDate(model.dueDate).build();
    }
}