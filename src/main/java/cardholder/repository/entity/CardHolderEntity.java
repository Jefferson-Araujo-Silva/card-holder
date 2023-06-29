package cardholder.repository.entity;

import cardholder.util.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import org.hibernate.annotations.Immutable;

@Table(name = "CARD_HOLDER")
@Entity
@Immutable
public class CardHolderEntity {
    @Id
    UUID id;
    UUID clientId;
    BigDecimal creditLimit;
    BigDecimal withdrawalLimit;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status activeStatus;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id", nullable = true)
    BankAccountEntity bankAccount;
    UUID creditAnalysisId;
    @Column(name = "created_at")
    LocalDateTime creationTime;

    public CardHolderEntity() {
    }

    @Builder(toBuilder = true)
    public CardHolderEntity(UUID id, UUID clientId, BigDecimal creditLimit, BigDecimal withdrawalLimit, Status activeStatus,
                            BankAccountEntity bankAccount, UUID creditAnalysisId) {
        this.id = Objects.requireNonNullElseGet(id, UUID::randomUUID);
        this.id = UUID.randomUUID();
        this.clientId = clientId;
        this.creditLimit = creditLimit;
        this.withdrawalLimit = withdrawalLimit;
        this.activeStatus = activeStatus;
        this.bankAccount = bankAccount;
        this.creditAnalysisId = creditAnalysisId;
        this.creationTime = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public BigDecimal getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public Status getActiveStatus() {
        return activeStatus;
    }

    public BankAccountEntity getBankAccount() {
        return bankAccount;
    }

    public UUID getCreditAnalysisId() {
        return creditAnalysisId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}
