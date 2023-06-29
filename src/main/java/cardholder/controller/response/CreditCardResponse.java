package cardholder.controller.response;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

public record CreditCardResponse(UUID cardId, String cardNumber, Integer cvv, LocalDate dueDate) {
    @Builder(toBuilder = true)
    public CreditCardResponse(UUID cardId, String cardNumber, Integer cvv, LocalDate dueDate) {
        this.cardId = cardId;
        this.cardNumber = maskCardNumber(cardNumber);
        this.cvv = cvv;
        this.dueDate = dueDate;
    }

    public String maskCardNumber(String cardNumber) {
        final StringBuilder maskedCardNumber = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i += 4) {
            if (i > 0) {
                maskedCardNumber.append(" ");
            }
            maskedCardNumber.append(cardNumber, i, Math.min(i + 4, cardNumber.length()));
        }

        return maskedCardNumber.toString();
    }
}