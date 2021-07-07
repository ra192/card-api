package com.card.controller;

import com.card.entity.Card;
import com.card.entity.enums.CardType;
import com.card.service.AccountService;
import com.card.service.CardService;
import com.card.service.CustomerService;
import com.card.controller.dto.CardDto;
import com.card.controller.dto.CreateCardDto;
import com.card.controller.dto.CreateCardTransactionDto;
import com.card.controller.dto.TransactionDto;
import com.card.service.exception.AccountException;
import com.card.service.exception.CardException;
import com.card.service.exception.CustomerException;
import com.card.service.exception.TransactionException;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card")
public class CardController extends BaseController {
    private final CardService cardService;
    private final AccountService accountService;
    private final CustomerService customerService;

    public CardController(CardService cardService, AccountService accountService, CustomerService customerService) {
        this.cardService = cardService;
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @PostMapping
    public CardDto createVirtual(@RequestBody CreateCardDto requestObject) throws CustomerException, AccountException {
        final var card = new Card(CardType.VIRTUAL, customerService.findActiveById(requestObject.getCustomerId()),
                accountService.findActiveById(requestObject.getAccountId()));
        cardService.create(card);

        final var result = new CardDto();
        BeanUtils.copyProperties(card, result);

        return result;
    }

    @PostMapping("/deposit")
    public TransactionDto deposit(@RequestBody CreateCardTransactionDto requestObject) throws TransactionException, CardException {
        final var transaction = cardService.deposit(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(),transaction.getStatus());
    }

    @PostMapping("/withdraw")
    public TransactionDto withdraw(@RequestBody CreateCardTransactionDto requestObject) throws CardException {
        final var transaction = cardService.withdraw(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(),transaction.getStatus());
    }
}