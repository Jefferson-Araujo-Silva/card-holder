package cardholder.repository;

import cardholder.repository.entity.CardHolderEntity;
import cardholder.model.statusenum.Status;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
    List<CardHolderEntity> findAllByActiveStatus(Status status);
}
