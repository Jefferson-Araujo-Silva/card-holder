package cardholder.mapper;

import cardholder.model.CreditCardModel;
import cardholder.repository.entity.CreditCardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CreditCardEntityMapper {
    @Mapping(source = "limit", target = "creditLimit")
    CreditCardEntity from(CreditCardModel model);
}
