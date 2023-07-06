package cardholder.mapper;

import cardholder.controller.request.CreditCardRequest;
import cardholder.model.CreditCardModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CreditCardModelMapper {
    CreditCardModel from(CreditCardRequest request);
}
