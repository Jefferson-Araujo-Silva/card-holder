package cardholder.repository.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface CreditCardRepository extends JpaRepository<CreditCardEntity, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE CreditCardEntity c SET c.creditLimit = :creditLimit WHERE c.id = :id")
    void updateLimitFromId(UUID id, BigDecimal creditLimit);

    @Query("SELECT SUM(c.creditLimit) FROM CreditCardEntity c WHERE c.cardHolder.id = :cardHolderId")
    BigDecimal getTotalCreditLimitByCardHolderId(@Param("cardHolderId") UUID cardHolderId);

    @Query("SELECT SUM(c.creditLimit) FROM CreditCardEntity c WHERE c.cardHolder.id = :cardHolderId AND c.id != COALESCE(:creditCardId, c.id)")
    BigDecimal getTotalCreditLimitByCardHolderId(@Param("cardHolderId") UUID cardHolderId, @Param("creditCardId") UUID creditCardId);

    List<CreditCardEntity> findAllByCardHolderId(UUID cardHolderId);
}
