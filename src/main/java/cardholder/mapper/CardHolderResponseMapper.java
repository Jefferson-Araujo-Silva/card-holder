package cardholder.mapper;

import cardholder.controller.response.CardHolderResponse;
import cardholder.repository.entity.CardHolderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CardHolderResponseMapper {
    @Mapping(source = "id", target = "cardHolderId")
    @Mapping(source = "activeStatus", target = "status")
    @Mapping(source = "creditLimit", target = "limit")
    @Mapping(source = "creationTime", target = "createdAt")
    CardHolderResponse from(CardHolderEntity cardHolderEntity);
}
