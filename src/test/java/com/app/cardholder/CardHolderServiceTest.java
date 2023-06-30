package com.app.cardholder;

import static org.mockito.Mockito.when;

import cardholder.api.CardHolderApiAnalysis;
import cardholder.api.analysisdto.CreditAnalysisDto;
import cardholder.controller.request.CardHolderRequest;
import cardholder.controller.response.CardHolderResponse;
import cardholder.exception.CardHolderAlreadyExistsException;
import cardholder.exception.ClientNotCorrespondsException;
import cardholder.exception.CreditAnalysisNotFound;
import cardholder.exception.CardHolderNotFoundException;
import cardholder.exception.NoCreditAnalysisApprovedException;
import cardholder.mapper.CardHolderEntityMapper;
import cardholder.mapper.CardHolderEntityMapperImpl;
import cardholder.mapper.CardHolderModelMapper;
import cardholder.mapper.CardHolderModelMapperImpl;
import cardholder.mapper.CardHolderResponseMapper;
import cardholder.mapper.CardHolderResponseMapperImpl;
import cardholder.model.BankAccountModel;
import cardholder.model.CardHolderModel;
import cardholder.repository.CardHolderRepository;
import cardholder.repository.entity.BankAccountEntity;
import cardholder.repository.entity.CardHolderEntity;
import cardholder.service.CardHolderService;
import cardholder.util.Status;
import feign.FeignException;
import feign.Request;
import feign.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CardHolderServiceTest {
    @InjectMocks
    private CardHolderService cardHolderService;
    @Mock
    private CardHolderRepository cardHolderRepository;

    @Mock
    private CardHolderApiAnalysis cardHolderApiAnalysis;
    @Spy
    private CardHolderModelMapper cardHolderModelMapper = new CardHolderModelMapperImpl();
    @Spy
    private CardHolderResponseMapper cardHolderResponseMapper = new CardHolderResponseMapperImpl();
    @Spy
    private CardHolderEntityMapper cardHolderRequestMapper = new CardHolderEntityMapperImpl();
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    private ArgumentCaptor<Status> statusArgumentCaptor;
    @Captor
    private ArgumentCaptor<CardHolderEntity> cardHolderEntityArgumentCaptor;

    public static CreditAnalysisDto creditAnalysisDtoFactory() {
        return CreditAnalysisDto.builder().id(UUID.fromString("a6c4c4ba-f780-4eb8-bcea-0c14ea2132bf")).date(LocalDateTime.now()).approved(true)
                .withdrawalLimitValue(new BigDecimal("100.0")).approvedLimit(new BigDecimal("1000.0"))
                .clientId(UUID.fromString("67f75dc9-e340-425c-b338-c58536097421")).build();
    }

    public static CardHolderRequest cardHolderRequestFactory() {
        CardHolderRequest.BankAccountRequest bankAccountRequest =
                CardHolderRequest.BankAccountRequest.builder().account("27184771-6").agency("1234").bankCode("123").build();
        return CardHolderRequest.builder().bankAccount(bankAccountRequest).creditAnalysisId(UUID.fromString("a6c4c4ba-f780-4eb8-bcea-0c14ea2132bf"))
                .clientId(UUID.fromString("67f75dc9-e340-425c-b338-c58536097421")).build();
    }

    public static CardHolderModel cardHolderModelFactory() {
        return CardHolderModel.builder().activeStatus(Status.ACTIVE).bankAccount(bankAccountModelFactory()).withdrawalLimit(new BigDecimal("100.0"))
                .creditLimit(new BigDecimal("1000.0")).clientId(UUID.fromString("67f75dc9-e340-425c-b338-c58536097421"))
                .creditAnalysisId(UUID.fromString("a6c4c4ba-f780-4eb8-bcea-0c14ea2132bf")).build();
    }

    public static CardHolderEntity cardHolderEntityFactory() {
        return CardHolderEntity.builder().activeStatus(Status.ACTIVE).withdrawalLimit(new BigDecimal("100.0"))
                .clientId(UUID.fromString("67f75dc9-e340-425c-b338-c58536097421")).creditLimit(new BigDecimal("1000.0"))
                .bankAccount(bankAccountEntityFactory()).creditAnalysisId(UUID.fromString("a6c4c4ba-f780-4eb8-bcea-0c14ea2132bf")).build();
    }

    public static BankAccountModel bankAccountModelFactory() {
        return BankAccountModel.builder().account("27184771-6").bankCode("302").agency("1121").build();
    }

    public static BankAccountEntity bankAccountEntityFactory() {
        return BankAccountEntity.builder().account("27184771-6").bankCode("302").agency("1121").build();
    }

    @Test
    public void should_return_card_holder() {
        when(cardHolderApiAnalysis.getCreditAnalysis(uuidArgumentCaptor.capture())).thenReturn(List.of(creditAnalysisDtoFactory()));
        when(cardHolderRepository.save(cardHolderEntityArgumentCaptor.capture())).thenReturn(cardHolderEntityFactory());
        CardHolderResponse cardHolderResponse = cardHolderService.createNewCardHolder(cardHolderRequestFactory());

        Assertions.assertNotNull(cardHolderResponse);
        Assertions.assertNotNull(cardHolderResponse.cardHolderId());

        Assertions.assertEquals(cardHolderResponse.limit(), cardHolderEntityArgumentCaptor.getValue().getCreditLimit());
        Assertions.assertEquals(UUID.fromString("a6c4c4ba-f780-4eb8-bcea-0c14ea2132bf"),
                cardHolderEntityArgumentCaptor.getValue().getCreditAnalysisId());
    }

    @Test
    public void should_throws_NoCreditAnalysisApprovedException_if_credit_analysis_is_not_approved() {
        when(cardHolderApiAnalysis.getCreditAnalysis(uuidArgumentCaptor.capture())).thenReturn(List.of(creditAnalysisDtoFactory()
                .toBuilder().id(UUID.fromString("a6c4c4ba-f780-4eb8-bcea-0c14ea2132bf")).approved(false).build()));

        NoCreditAnalysisApprovedException exception = Assertions.assertThrows(NoCreditAnalysisApprovedException.class,
                () -> cardHolderService.createNewCardHolder(cardHolderRequestFactory()));
        Assertions.assertEquals("Credit analysis with id %s is not approved".formatted(UUID.fromString("a6c4c4ba-f780-4eb8-bcea-0c14ea2132bf")),
                exception.getMessage());
    }

    @Test
    public void should_throws_CardHolderAlreadyExistsException_if_card_holder_already_exists() {
        when(cardHolderApiAnalysis.getCreditAnalysis(uuidArgumentCaptor.capture())).thenReturn(List.of(creditAnalysisDtoFactory()));
        when(cardHolderRepository.save(cardHolderEntityArgumentCaptor.capture())).thenThrow(DataIntegrityViolationException.class);

        CardHolderAlreadyExistsException exception = Assertions.assertThrows(CardHolderAlreadyExistsException.class,
                () -> cardHolderService.createNewCardHolder(cardHolderRequestFactory()));
        Assertions.assertEquals("Card Holder already registered, check the data sent for registration", exception.getMessage());
    }
    @Test
    public void should_return_all_card_holders_by_status() {
        when(cardHolderRepository.findAllByActiveStatus(statusArgumentCaptor.capture())).thenReturn(List.of(cardHolderEntityFactory()));

        List<CardHolderResponse> cardHolderResponses = cardHolderService.getCardHolderByStatus("ACTIVE");
        Assertions.assertEquals(Status.ACTIVE, statusArgumentCaptor.getValue());
        Assertions.assertEquals(1, cardHolderResponses.size());
        Assertions.assertEquals("ACTIVE", cardHolderResponses.get(0).status());
    }

    @Test
    public void should_throws_CardHolderNotFoundException_if_card_holder_not_found() {
        List<CardHolderEntity> cardHolderEntitiesEmpty = List.of();
        when(cardHolderRepository.findAllByActiveStatus(statusArgumentCaptor.capture())).thenReturn(cardHolderEntitiesEmpty);
        CardHolderNotFoundException exception =
                Assertions.assertThrows(CardHolderNotFoundException.class, () -> cardHolderService.getCardHolderByStatus("INACTIVE"));
        Assertions.assertEquals("CardHolder not found by status INACTIVE", exception.getMessage());
    }
    @Test
    public void should_return_all_card_holders() {
        when(cardHolderRepository.findAll()).thenReturn(List.of(cardHolderEntityFactory()));
        List<CardHolderResponse> cardHolderResponses = cardHolderService.getAllCardHolders();

        Assertions.assertNotNull(cardHolderResponses);
        Assertions.assertEquals(1, cardHolderResponses.size());
        Assertions.assertEquals("ACTIVE", cardHolderResponses.get(0).status());
    }

    @Test
    public void should_throws_ClientNotCorrespondsException_when_client_id_card_holder_request_not_corresponds_with_client_id_in_credit_analysis() {
        CardHolderRequest request = cardHolderRequestFactory().toBuilder().clientId(UUID.randomUUID()).build();
        CreditAnalysisDto creditAnalysisDto = creditAnalysisDtoFactory().toBuilder().clientId(UUID.randomUUID()).build();
        when(cardHolderApiAnalysis.getCreditAnalysis(uuidArgumentCaptor.capture())).thenReturn(List.of(creditAnalysisDto));

        ClientNotCorrespondsException exception =
                Assertions.assertThrows(ClientNotCorrespondsException.class, () -> cardHolderService.createNewCardHolder(request));
        Assertions.assertEquals(
                "Client with id %s not corresponds with credit analysis with id %s".formatted(request.clientId(), creditAnalysisDto.id()),
                exception.getMessage());
    }
    @Test
    public void should_throws_CreditAnalysisNotFound_when_api_credit_analysis_return_an_feign_exception_with_status_404(){
        FeignException feignException = FeignException.errorStatus("Credit analysis not found",
                Response.builder()
                        .status(404)
                        .reason("Not Found")
                        .request(Request.create(Request.HttpMethod.GET, "", Map.of(), null, null, null))
                        .build());
        when(cardHolderApiAnalysis.getCreditAnalysis(uuidArgumentCaptor.capture())).thenThrow(feignException);

        CreditAnalysisNotFound exception = Assertions.assertThrows(CreditAnalysisNotFound.class,
                () -> cardHolderService.createNewCardHolder(cardHolderRequestFactory()));
        Assertions.assertEquals("Credit analysis with id %s not found".formatted(uuidArgumentCaptor.getValue()), exception.getMessage());
    }
    @Test
    public void should_throw_feign_exception_if_returned_any_status_different_of_404_on_api_credit_analysis(){
        FeignException feignException = FeignException.errorStatus("Credit analysis not found",
                Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(Request.create(Request.HttpMethod.GET, "", Map.of(), null, null, null))
                        .build());
        when(cardHolderApiAnalysis.getCreditAnalysis(uuidArgumentCaptor.capture())).thenThrow(feignException);

        Assertions.assertThrows(FeignException.class, () -> cardHolderService.createNewCardHolder(cardHolderRequestFactory()));
    }
}