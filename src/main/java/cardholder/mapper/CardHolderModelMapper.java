package cardholder.mapper;

import cardholder.api.analysisdto.CreditAnalysisDto;
import cardholder.controller.request.CardHolderRequest;
import cardholder.model.BankAccountModel;
import cardholder.model.CardHolderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CardHolderModelMapper {
    CardHolderModel from(CardHolderRequest request);

    @Mapping(source = "withdrawalLimitValue", target = "withdrawalLimit")
    @Mapping(source = "approvedLimit", target = "creditLimit")
    @Mapping(source = "id", target = "creditAnalysisId")
    CardHolderModel from(CreditAnalysisDto creditAnalysisDto);

    BankAccountModel from(CardHolderRequest.BankAccountRequest request);
}
