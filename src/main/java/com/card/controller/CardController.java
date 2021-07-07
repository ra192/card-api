package com.card.controller;

import com.card.entity.Card;
import com.card.entity.Merchant;
import com.card.entity.enums.CardType;
import com.card.service.*;
import com.card.controller.dto.CardDto;
import com.card.controller.dto.CreateCardDto;
import com.card.controller.dto.CreateCardTransactionDto;
import com.card.controller.dto.TransactionDto;
import com.card.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card")
public class CardController extends WithAuthMerchantController {
    private static final Logger logger= LoggerFactory.getLogger(CustomerController.class);

    private final CardService cardService;
    private final AccountService accountService;
    private final CustomerService customerService;

    public CardController(CardService cardService, AccountService accountService, CustomerService customerService,
                          TokenService tokenService, MerchantService merchantService) {
        super(tokenService, merchantService);
        this.cardService = cardService;
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @PostMapping
    public CardDto createVirtual(@RequestHeader String authorization, @RequestBody CreateCardDto requestObject)
            throws CustomerException, AccountException, MerchantException, TokenException {
        logger.info("Create virtual card method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);
        final var card = new Card(CardType.VIRTUAL, customerService.findActiveById(requestObject.getCustomerId()),
                accountService.findActiveById(requestObject.getAccountId()));
        cardService.create(card);

        final var result = new CardDto();
        BeanUtils.copyProperties(card, result);

        return result;
    }

    @PostMapping("/deposit")
    public TransactionDto deposit(@RequestHeader String authorization, @RequestBody CreateCardTransactionDto requestObject)
            throws TransactionException, CardException, MerchantException, TokenException {
        logger.info("Card deposit method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);
        final var transaction = cardService.deposit(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }

    @PostMapping("/withdraw")
    public TransactionDto withdraw(@RequestHeader String authorization, @RequestBody CreateCardTransactionDto requestObject)
            throws CardException, MerchantException, TokenException {
        logger.info("Card withdraw method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);
        final var transaction = cardService.withdraw(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }
}