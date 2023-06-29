package cardholder.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Entity
@Table(name = "CREDIT_CARD")
public class CreditCardEntity {
    @Id
    UUID id;
    @ManyToOne
    @JoinColumn(name = "card_holder_id", referencedColumnName = "id")
    CardHolderEntity cardHolder;
    @Column(name = "credit_limit")
    BigDecimal creditLimit;
    @Column(name = "card_number")
    String cardNumber;
    @Column(name = "cvv")
    String cvv;
    @Column(name = "due_date")
    LocalDate dueDate;
    @Column(name = "created_at")
    LocalDateTime createdAt;

    public CreditCardEntity() {
    }

    @Builder(toBuilder = true)
    public CreditCardEntity(CardHolderEntity cardHolder, BigDecimal creditLimit, String cardNumber, String cvv, LocalDate dueDate) {
        this.id = UUID.randomUUID();
        this.cardHolder = cardHolder;
        this.creditLimit = creditLimit;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public CardHolderEntity getCardHolder() {
        return cardHolder;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}