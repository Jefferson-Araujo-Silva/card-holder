package cardholder.service;

import cardholder.controller.request.CreditCardRequest;
import cardholder.controller.response.CreditCardResponse;
import cardholder.controller.response.CreditCardUpdateLimitResponse;
import cardholder.exception.CreditCardNotFoundException;
import cardholder.exception.NegativeValueException;
import cardholder.exception.NoLimitAvailableException;
import cardholder.exception.ThresholdValueRequestException;
import cardholder.mapper.CreditCardEntityMapper;
import cardholder.mapper.CreditCardModelMapper;
import cardholder.mapper.CreditCardResponseMapper;
import cardholder.model.CreditCardModel;
import cardholder.repository.entity.CardHolderEntity;
import cardholder.repository.entity.CreditCardEntity;
import cardholder.repository.entity.CreditCardRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditCardService {
    private final CreditCardRepository repository;
    private final CreditCardModelMapper modelMapper;
    private final CreditCardResponseMapper responseMapper;
    private final CreditCardEntityMapper entityMapper;
    private final CardHolderService cardHolderService;

    public CreditCardResponse createNewCreditCard(UUID cardHolderId, CreditCardRequest request) {
        final CreditCardModel creditCardModel = modelMapper.from(request);
        final CardHolderEntity cardHolderResponse = getCardHolderById(cardHolderId);
        final CreditCardModel creditCardModelUpdated = creditCardModel.updateCreditCardModel(creditCardModel, cardHolderResponse);
        final CreditCardModel creditCardModelVerified = verifyCreditCardLimitRequested(creditCardModelUpdated, cardHolderResponse);
        final CreditCardEntity creditCardToBeSaved = entityMapper.from(creditCardModelVerified);
        final CreditCardEntity creditCardEntitySaved = saveCreditCard(creditCardToBeSaved);
        return responseMapper.from(creditCardEntitySaved);
    }

    private CardHolderEntity getCardHolderById(UUID cardHolderId) {
        return cardHolderService.getCardHolderById(cardHolderId);
    }

    private CreditCardModel verifyCreditCardLimitRequested(CreditCardModel creditCardModel, CardHolderEntity cardHolderResponse) {
        verifyLimitRequestedComparedByCreditLimit(creditCardModel.limit(), cardHolderResponse.getCreditLimit());
        calculateCreditLimitAvailableForCardHolder(creditCardModel.cardHolder().getId(), cardHolderResponse.getCreditLimit(),
                creditCardModel.limit());

        return creditCardModel;
    }

    private void verifyLimitRequestedComparedByCreditLimit(BigDecimal limitRequested, BigDecimal creditLimit) {
        if (limitRequested.compareTo(creditLimit) > 0) {
            throw new ThresholdValueRequestException("Limit requested is greater than card holder limit");
        }
    }

    private void calculateCreditLimitAvailableForCardHolder(UUID cardHolderId, BigDecimal creditLimitForCardHolder, BigDecimal creditLimitRequested) {
        final List<CreditCardEntity> creditCardEntities = repository.findAllByCardHolderId(cardHolderId);
        BigDecimal creditLimitAvailable = creditLimitForCardHolder;
        for (CreditCardEntity creditCardEntity : creditCardEntities) {
            creditLimitAvailable = creditLimitAvailable.subtract(creditCardEntity.getCreditLimit());
        }
        if (creditLimitRequested.compareTo(creditLimitAvailable) > 0) {
            throw new NoLimitAvailableException("No limit available for card holder with id %s".formatted(cardHolderId));
        }
    }

    private void calculateCreditLimitAvailableForCardHolder(UUID cardHolderId, BigDecimal creditLimitForCardHolder, UUID creditCardId,
                                                            BigDecimal creditLimitRequested) {
        final List<CreditCardEntity> creditCardEntities = repository.findAllByCardHolderId(cardHolderId);
        final List<CreditCardEntity> creditCardEntityListWithoutCreditCardIdPassed =
                creditCardEntities.stream().filter(creditCardEntity -> !creditCardEntity.getId().equals(creditCardId)).toList();

        BigDecimal creditLimitAvailable = creditLimitForCardHolder;
        for (CreditCardEntity creditCardEntity : creditCardEntityListWithoutCreditCardIdPassed) {
            creditLimitAvailable = creditLimitAvailable.subtract(creditCardEntity.getCreditLimit());
        }
        if (creditLimitRequested.compareTo(creditLimitAvailable) > 0) {
            throw new NoLimitAvailableException("No limit available for card holder with id %s".formatted(cardHolderId));
        }
    }


    private CreditCardEntity saveCreditCard(CreditCardEntity entity) {
        return repository.save(entity);
    }

    public List<CreditCardResponse> getCreditCardsByCardHolderId(UUID cardHolderId) {
        final List<CreditCardEntity> creditCardEntities = repository.findAllByCardHolderId(cardHolderId);
        if (creditCardEntities.isEmpty()) {
            throw new CreditCardNotFoundException(
                    "No credit cards found for card holder with id %s, or card holder not exists".formatted(cardHolderId));
        }
        return creditCardEntities.stream().map(responseMapper::from).collect(Collectors.toList());
    }

    public CreditCardResponse getCreditCardsByCreditCardId(UUID cardHolderId, UUID creditCardId) {
        final CreditCardEntity entity = getCreditCardEntityByCreditCardId(cardHolderId, creditCardId);
        return responseMapper.from(entity);
    }

    private CreditCardEntity getCreditCardEntityByCreditCardId(UUID cardHolderId, UUID creditCardId) {
        final List<CreditCardEntity> creditCardEntities = repository.findAllByCardHolderId(cardHolderId);
        if (creditCardEntities.isEmpty()) {
            throw new CreditCardNotFoundException(
                    "No credit cards found for card holder with id %s, or card holder not exists".formatted(cardHolderId));
        }

        return creditCardEntities.stream().filter(e -> e.getId().equals(creditCardId)).findFirst().orElseThrow(() -> new CreditCardNotFoundException(
                "No credit cards found with id %s for card holder with id %s, or card holder not exists".formatted(creditCardId, cardHolderId)));
    }

    public CreditCardUpdateLimitResponse updateCreditCardLimit(UUID cardHolderId, UUID creditCardId, BigDecimal limit) {
        verifyLimitToBeUpdated(cardHolderId, creditCardId, limit);
        final CreditCardEntity creditCardEntity = getCreditCardEntityByCreditCardId(cardHolderId, creditCardId);
        return CreditCardUpdateLimitResponse.builder().cardId(creditCardEntity.getId()).updatedLimit(creditCardEntity.getCreditLimit()).build();
    }

    private void verifyLimitToBeUpdated(UUID cardHolderId, UUID creditCardId, BigDecimal limit) {
        if (limit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeValueException("Limit requested is less than zero");
        }
        final CardHolderEntity cardHolder = getCardHolderById(cardHolderId);
        verifyLimitRequestedComparedByCreditLimit(limit, cardHolder.getCreditLimit());
        calculateCreditLimitAvailableForCardHolder(cardHolderId, cardHolder.getCreditLimit(), creditCardId, limit);
        repository.updateLimitFromId(creditCardId, limit);
    }
}
