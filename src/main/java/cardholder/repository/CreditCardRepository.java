package cardholder.repository;

import cardholder.repository.entity.CreditCardEntity;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity, UUID> {
    @Query("SELECT SUM(c.creditLimit) FROM CreditCardEntity c WHERE c.cardHolder.id = :cardHolderId")
    BigDecimal getTotalCreditLimitByCardHolderId(@Param("cardHolderId") UUID cardHolderId);
}
