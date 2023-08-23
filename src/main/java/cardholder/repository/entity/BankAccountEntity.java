package cardholder.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "BANK_ACCOUNT")
@Immutable
public class BankAccountEntity {
    @Id
    UUID id;
    String agency;
    String bankCode;
    @Column(name = "account_number")
    String account;

    public BankAccountEntity() {
    }

    @Builder(toBuilder = true)
    public BankAccountEntity(String agency, String bankCode, String account) {
        this.id = UUID.randomUUID();
        this.agency = agency;
        this.bankCode = bankCode;
        this.account = account;
    }

    public UUID getId() {
        return id;
    }

    public String getAgency() {
        return agency;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getAccount() {
        return account;
    }
}