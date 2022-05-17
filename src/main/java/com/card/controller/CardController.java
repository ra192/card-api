package com.card.controller;

import com.card.service.dto.CardDTO;
import com.card.controller.dto.CreateCardTransactionDto;
import com.card.controller.dto.TransactionDto;
import com.card.service.*;
import com.card.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card")
public class CardController extends WithAuthMerchantController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

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
    public CardDTO createVirtual(@RequestHeader String authorization, @RequestBody CardDTO cardDTO)
            throws CustomerException, AccountException, MerchantException {
        logger.info("Create virtual card method was called with params:");
        logger.info(cardDTO.toString());

        validateToken(authorization);

        return cardService.create(cardDTO);
    }

    @PostMapping("/deposit")
    public TransactionDto deposit(@RequestHeader String authorization, @RequestBody CreateCardTransactionDto requestObject)
            throws TransactionException, CardException, MerchantException, AccountException {
        logger.info("Card deposit method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);
        final var transaction = cardService.deposit(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }

    @PostMapping("/withdraw")
    public TransactionDto withdraw(@RequestHeader String authorization, @RequestBody CreateCardTransactionDto requestObject)
            throws CardException, MerchantException, AccountException, TransactionException {
        logger.info("Card withdraw method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);
        final var transaction = cardService.withdraw(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }
}