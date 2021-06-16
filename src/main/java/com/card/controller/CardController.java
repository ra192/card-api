package com.card.controller;

import com.card.controller.dto.CreateCardResultDto;
import com.card.controller.dto.TransactionResultDto;
import com.card.entity.Card;
import com.card.service.CardService;
import com.card.service.exception.AccountException;
import com.card.service.exception.CardException;
import com.card.service.exception.CustomerException;
import com.card.service.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/issue/virtual")
    public CreateCardResultDto createVirtual(@RequestParam Long accountId, @RequestParam Long customerId) {
        final Card card;
        try {
            card = cardService.createVirtual(customerId, accountId);
            return new CreateCardResultDto("ok", "", card.getId());
        } catch (AccountException | CustomerException e) {
            log.error(e.getMessage());
            return new CreateCardResultDto("error", e.getMessage(), 0L);
        }
    }

    @GetMapping("/deposit")
    public TransactionResultDto deposit(@RequestParam Long cardId, @RequestParam Long amount,
                                        @RequestParam String orderId) {
        try {
            final var transaction = cardService.deposit(cardId, amount, orderId);

            return new TransactionResultDto("ok", "", transaction.getId());
        } catch (CardException | TransactionException e) {
            log.error(e.getMessage());
            return new TransactionResultDto("error", e.getMessage(), 0L);
        }
    }

    @GetMapping("/withdraw")
    public TransactionResultDto withdraw(@RequestParam Long cardId, @RequestParam Long amount, @RequestParam String orderId) {
        try {
            final var transaction = cardService.withdraw(cardId, amount, orderId);

            return new TransactionResultDto("ok", "", transaction.getId());
        } catch (CardException e) {
            log.error(e.getMessage());
            return new TransactionResultDto("error", e.getMessage(), 0L);
        }
    }
}