package cardholder.mapper;

import cardholder.controller.response.CreditCardResponse;
import cardholder.repository.entity.CreditCardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CreditCardResponseMapper {
    @Mapping(source = "id", target = "cardId")
    CreditCardResponse from(CreditCardEntity entity);
}
