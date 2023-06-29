package cardholder.repository.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE CreditCardEntity c SET c.creditLimit = :creditLimit WHERE c.id = :id")
    void updateLimitFromId(UUID id, BigDecimal creditLimit);

    List<CreditCardEntity> findAllByCardHolderId(UUID cardHolderId);
}
