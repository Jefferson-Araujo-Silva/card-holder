package cardholder.controller;

import cardholder.controller.request.CardHolderRequest;
import cardholder.controller.request.CreditCardRequest;
import cardholder.controller.response.CardHolderResponse;
import cardholder.controller.response.CreditCardResponse;
import cardholder.service.CardHolderService;
import cardholder.service.CreditCardService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1.0/card-holders")
@RequiredArgsConstructor
public class CardHolderController {

    private static final String PATH_DEFAULT_ENDPOINT = "v1.0/card-holders";
    private static final Logger LOGGER = LoggerFactory.getLogger(CardHolderController.class);
    private final CardHolderService cardHolderService;
    private final CreditCardService creditCardService;

    @PostMapping
    public CardHolderResponse postNewCardHolder(@RequestBody CardHolderRequest request) {
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOGGER.info("Received an post requisition at endpoint %s".formatted(PATH_DEFAULT_ENDPOINT));
        return cardHolderService.createNewCardHolder(request);
    }

    @GetMapping
    public List<CardHolderResponse> getCardHolderBy(@RequestParam(value = "activeStatus", required = false) String activeStatus) {
        if (activeStatus != null) {
            MDC.put("correlationId", UUID.randomUUID().toString());
            LOGGER.info(
                    "Received an post requisition at get endpoint %s with filter ?activeStatus=%s".formatted(PATH_DEFAULT_ENDPOINT, activeStatus));
            return cardHolderService.getCardHolderByStatus(activeStatus);
        }
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOGGER.info("Received an post requisition at get endpoint %s".formatted(PATH_DEFAULT_ENDPOINT));
        return cardHolderService.getAllCardHolders();
    }

    @GetMapping(path = "/{id}")
    public CardHolderResponse getCardHolderById(@PathVariable(value = "id") UUID id) {
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOGGER.info("Received an get requisition at endpoint %s/%s".formatted(PATH_DEFAULT_ENDPOINT, id));
        return cardHolderService.findCardHolderById(id);
    }

    @PostMapping(path = "/{cardHolderId}/cards")
    public CreditCardResponse postNewCard(@PathVariable(value = "cardHolderId") UUID cardHolderId, @RequestBody CreditCardRequest request) {
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOGGER.info("Received an post requisition at endpoint %s/%s/cards".formatted(PATH_DEFAULT_ENDPOINT, cardHolderId));
        return creditCardService.createNewCreditCard(cardHolderId, request);
    }
}
