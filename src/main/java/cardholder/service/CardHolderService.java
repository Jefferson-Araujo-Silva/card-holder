package cardholder.service;

import cardholder.api.CardHolderApiAnalysis;
import cardholder.api.analysisdto.CreditAnalysisDto;
import cardholder.controller.request.CardHolderRequest;
import cardholder.controller.response.CardHolderResponse;
import cardholder.exception.CardHolderAlreadyExistsException;
import cardholder.exception.CardHolderNotFoundException;
import cardholder.exception.ClientNotCorrespondsException;
import cardholder.exception.CreditAnalysisNotFound;
import cardholder.exception.NoCreditAnalysisApprovedException;
import cardholder.mapper.CardHolderEntityMapper;
import cardholder.mapper.CardHolderModelMapper;
import cardholder.mapper.CardHolderResponseMapper;
import cardholder.model.CardHolderModel;
import cardholder.model.statusenum.Status;
import cardholder.repository.CardHolderRepository;
import cardholder.repository.entity.CardHolderEntity;
import feign.FeignException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardHolderService {
    private final CardHolderRepository repository;
    private final CardHolderModelMapper modelMapper;
    private final CardHolderResponseMapper responseMapper;
    private final CardHolderEntityMapper entityMapper;
    private final CardHolderApiAnalysis apiAnalysis;

    public CardHolderResponse createNewCardHolder(CardHolderRequest request) {
        final CardHolderModel cardHolderModel = modelMapper.from(request);
        final CreditAnalysisDto creditAnalysisDto = findCreditAnalysisFromId(cardHolderModel.creditAnalysisId(), cardHolderModel.clientId());
        final CardHolderModel modelWithInfo = cardHolderModel.updateCardHolderFrom(creditAnalysisDto);
        final CardHolderEntity cardHolderToBeSaved = entityMapper.from(modelWithInfo);
        final CardHolderEntity cardHolderEntitySaved = saveCardHolder(cardHolderToBeSaved);
        return responseMapper.from(cardHolderEntitySaved);
    }

    private CreditAnalysisDto findCreditAnalysisFromId(UUID idAnalysis, UUID clientId) {
        try {
            final CreditAnalysisDto creditAnalysis = apiAnalysis.getCreditAnalysis(idAnalysis).get(0);
            if (!creditAnalysis.approved()) {
                throw new NoCreditAnalysisApprovedException("Credit analysis with id %s is not approved".formatted(idAnalysis));
            }
            if (!creditAnalysis.clientId().equals(clientId)) {
                throw new ClientNotCorrespondsException(
                        "Client with id %s not corresponds with credit analysis with id %s".formatted(clientId, idAnalysis));
            }
            return creditAnalysis;
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new CreditAnalysisNotFound("Credit analysis with id %s not found".formatted(idAnalysis));
            } else {
                throw e;
            }
        }
    }

    private CardHolderEntity saveCardHolder(CardHolderEntity entity) {
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new CardHolderAlreadyExistsException("Card Holder already registered, check the data sent for registration");
        }
    }

    public List<CardHolderResponse> getCardHolderByStatus(String activeStatus) {
        final Status status = Status.valueOf(activeStatus);
        final List<CardHolderEntity> entity = repository.findAllByActiveStatus(status);
        if (entity.size() == 0) {
            throw new CardHolderNotFoundException("CardHolder not found by status %s".formatted(activeStatus));
        }
        return entity.stream().map(responseMapper::from).toList();
    }

    public List<CardHolderResponse> getAllCardHolders() {
        final List<CardHolderResponse> cardHolderResponses = repository.findAll().stream().map(responseMapper::from).toList();
        if (cardHolderResponses.size() == 0) {
            throw new CardHolderNotFoundException("No card holders registered");
        }
        return cardHolderResponses;
    }
}