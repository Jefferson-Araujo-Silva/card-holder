package cardholder.mapper;

import cardholder.model.BankAccountModel;
import cardholder.model.CardHolderModel;
import cardholder.repository.entity.BankAccountEntity;
import cardholder.repository.entity.CardHolderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CardHolderEntityMapper {
    CardHolderEntity from(CardHolderModel model);

    BankAccountEntity from(BankAccountModel bankAccountModel);
}
