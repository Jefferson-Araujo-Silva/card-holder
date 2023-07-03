package com.app.cardholder;

import static org.mockito.Mockito.when;

import cardholder.controller.request.CreditCardRequest;
import cardholder.controller.response.CreditCardResponse;
import cardholder.exception.CreditCardNotFoundException;
import cardholder.exception.NoLimitAvailableException;
import cardholder.exception.ThresholdValueRequestException;
import cardholder.mapper.CreditCardEntityMapper;
import cardholder.mapper.CreditCardEntityMapperImpl;
import cardholder.mapper.CreditCardModelMapper;
import cardholder.mapper.CreditCardModelMapperImpl;
import cardholder.mapper.CreditCardResponseMapper;
import cardholder.mapper.CreditCardResponseMapperImpl;
import cardholder.model.CreditCardModel;
import cardholder.repository.CardHolderRepository;
import cardholder.repository.entity.CardHolderEntity;
import cardholder.repository.entity.CreditCardEntity;
import cardholder.repository.entity.CreditCardRepository;
import cardholder.service.CardHolderService;
import cardholder.service.CreditCardService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CreditCardServiceTest {
    private static final CardHolderServiceTest cardHolderServiceTest = new CardHolderServiceTest();
    @InjectMocks
    private CreditCardService service;
    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private CardHolderRepository cardHolderRepository;
    @Mock
    private CardHolderService cardHolderService;
    @Spy
    private CreditCardModelMapper creditCardModelMapper = new CreditCardModelMapperImpl();
    @Spy
    private CreditCardResponseMapper creditCardResponseMapper = new CreditCardResponseMapperImpl();
    @Spy
    private CreditCardEntityMapper creditCardEntityMapper = new CreditCardEntityMapperImpl();
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    private ArgumentCaptor<CreditCardEntity> creditCardEntityArgumentCaptor;

    public static CreditCardModel creditCardModelFactory() {
        return CreditCardModel.builder().cardHolder(cardHolderEntityFactory()).build();
    }

    public static CreditCardEntity creditCardEntityFactory() {
        return CreditCardEntity.builder().cardHolder(CardHolderServiceTest.cardHolderEntityFactory()).cardNumber("4483346541790809").cvv("123")
                .dueDate(LocalDate.parse("2028-01-01")).creditLimit(new BigDecimal("100")).build();
    }

    public static CardHolderEntity cardHolderEntityFactory() {
        return CardHolderServiceTest.cardHolderEntityFactory();
    }

    public static CreditCardRequest creditCardRequestFactory() {
        return CreditCardRequest.builder().limit(new BigDecimal("1000")).build();
    }

    public static CreditCardResponse creditCardResponseFactory() {
        return CreditCardResponse.builder().cardId(UUID.fromString("5c62d902-e165-4cea-be3b-aae6b54a4cf4")).cardNumber("1234567890123456")
                .dueDate(LocalDate.parse("2021-09-01")).cvv(123).build();
    }

    @Test
    public void should_create_new_credit_card() {
        CardHolderEntity cardHolderEntity = cardHolderEntityFactory();
        when(cardHolderService.getCardHolderById(uuidArgumentCaptor.capture())).thenReturn(cardHolderEntity);
        when(creditCardRepository.save(creditCardEntityArgumentCaptor.capture())).thenReturn(creditCardEntityFactory());

        CreditCardResponse response = service.createNewCreditCard(cardHolderEntity.getId(), creditCardRequestFactory());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.cardId());
    }

    @Test
    public void should_ThresholdValueRequestException_when_limit_is_more_than_100() {
        CreditCardRequest request = creditCardRequestFactory().toBuilder().limit(new BigDecimal("101")).build();
        CardHolderEntity cardHolder = cardHolderEntityFactory().toBuilder().creditLimit(new BigDecimal("100")).build();
        when(cardHolderService.getCardHolderById(uuidArgumentCaptor.capture())).thenReturn(cardHolder);

        ThresholdValueRequestException exception =
                Assertions.assertThrows(ThresholdValueRequestException.class, () -> service.createNewCreditCard(cardHolder.getId(), request));

        Assertions.assertEquals("Limit requested is greater than card holder limit", exception.getMessage());
    }

    @Test
    public void should_throws_NoLimitAvailableException_when_limit_is_more_than_100() {
        CreditCardRequest request = creditCardRequestFactory().toBuilder().limit(new BigDecimal("10")).build();
        CardHolderEntity cardHolder = cardHolderEntityFactory().toBuilder().creditLimit(new BigDecimal("100")).build();
        when(cardHolderService.getCardHolderById(uuidArgumentCaptor.capture())).thenReturn(cardHolder);
        when(creditCardRepository.findAllByCardHolderId(uuidArgumentCaptor.capture())).thenReturn(
                List.of(creditCardEntityFactory().toBuilder().creditLimit(new BigDecimal("99")).build()));
        NoLimitAvailableException exception =
                Assertions.assertThrows(NoLimitAvailableException.class, () -> service.createNewCreditCard(cardHolder.getId(), request));

        Assertions.assertEquals("No limit available for card holder with id %s".formatted(uuidArgumentCaptor.getValue()), exception.getMessage());
    }

    @Test
    public void should_return_all_credit_cards_from_card_holder_id() {
        when(creditCardRepository.findAllByCardHolderId(uuidArgumentCaptor.capture())).thenReturn(List.of(creditCardEntityFactory()));

        List<CreditCardResponse> response = service.getCreditCardsByCardHolderId(cardHolderEntityFactory().getId());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
    }

    @Test
    public void should_throws_CreditCardNotFoundException_when_return_nothing() {
        when(creditCardRepository.findAllByCardHolderId(uuidArgumentCaptor.capture())).thenReturn(List.of());

        CreditCardNotFoundException exception = Assertions.assertThrows(CreditCardNotFoundException.class,
                () -> service.getCreditCardsByCardHolderId(cardHolderEntityFactory().getId()));

        Assertions.assertEquals(
                "No credit cards found for card holder with id %s, or card holder not exists".formatted(uuidArgumentCaptor.getValue()),
                exception.getMessage());
    }

    @Test
    public void should_return_one_credit_card_from_card_id() {
        CreditCardEntity creditCardEntity = creditCardEntityFactory();
        when(creditCardRepository.findById(uuidArgumentCaptor.capture())).thenReturn(Optional.of(creditCardEntity));

        CreditCardResponse response = service.getCreditCardsByCreditCardId(creditCardEntity.getCardHolder().getId(), creditCardEntity.getId());

        Assertions.assertNotNull(response);
    }

    @Test
    public void should_throws_CreditCardNotFoundException_when_return_nothing_on_getByCardId() {
        when(creditCardRepository.findById(uuidArgumentCaptor.capture())).thenReturn(Optional.empty());
        UUID creditCardId = UUID.randomUUID();
        CardHolderEntity cardHolder = cardHolderEntityFactory();
        CreditCardNotFoundException exception = Assertions.assertThrows(CreditCardNotFoundException.class,
                () -> service.getCreditCardsByCreditCardId(cardHolder.getId(), creditCardId));

        Assertions.assertEquals(
                "No credit card found with id %s for card holder with id %s, or card holder not exists".formatted(uuidArgumentCaptor.getValue(),
                        cardHolder.getId()), exception.getMessage());
    }

    @Test
    public void should_throws_CreditCardNotFoundException_when_id_not_found_on_list() {
        when(creditCardRepository.findById(uuidArgumentCaptor.capture())).thenReturn(Optional.of(creditCardEntityFactory()));
        UUID creditCardId = UUID.randomUUID();
        CardHolderEntity  cardHolder = cardHolderEntityFactory();
        CreditCardNotFoundException exception = Assertions.assertThrows(CreditCardNotFoundException.class,
                () -> service.getCreditCardsByCreditCardId(cardHolder.getId(), creditCardId));

        Assertions.assertEquals("No credit card found with id %s for card holder with id %s, or card holder not exists".formatted(uuidArgumentCaptor.getValue(),
                cardHolder.getId()), exception.getMessage());
    }
}