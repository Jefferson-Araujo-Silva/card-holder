package cardholder.controller;

import cardholder.controller.request.CardHolderRequest;
import cardholder.controller.response.CardHolderResponse;
import cardholder.service.CardHolderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1.0/card-holders")
@RequiredArgsConstructor
public class CardHolderController {

    private static final String PATH_DEFAULT_ENDPOINT = "v1.0/card-holders";
    private static final Logger LOGGER = LoggerFactory.getLogger(CardHolderController.class);
    private final CardHolderService cardHolderService;

    @PostMapping
    public CardHolderResponse postNewCardHolder(@RequestBody CardHolderRequest request) {
        MDC.put("correlationId", UUID.randomUUID().toString());
        LOGGER.info("Received an post requisition at endpoint %s".formatted(PATH_DEFAULT_ENDPOINT));
        return cardHolderService.createNewCardHolder(request);
    }
}
