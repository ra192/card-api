package com.card.controller;

import com.card.service.dto.CardDTO;
import com.card.controller.objs.CreateCardTransactionRequest;
import com.card.service.dto.TransactionDTO;
import com.card.service.*;
import com.card.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card")
public class CardController extends WithAuthMerchantController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CardService cardService;

    public CardController(CardService cardService, TokenService tokenService, MerchantService merchantService) {
        super(tokenService, merchantService);
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardDTO> createVirtual(@RequestHeader String authorization, @RequestBody CardDTO cardDTO)
            throws CustomerException, AccountException, MerchantException {
        logger.info("Create virtual card method was called with params:");
        logger.info(cardDTO.toString());

        validateToken(authorization);

        return ResponseEntity.ok(cardService.create(cardDTO));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDTO> deposit(@RequestHeader String authorization, @RequestBody CreateCardTransactionRequest requestObject)
            throws TransactionException, CardException, MerchantException, AccountException {
        logger.info("Card deposit method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);

        return ResponseEntity.ok(cardService.deposit(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@RequestHeader String authorization, @RequestBody CreateCardTransactionRequest requestObject)
            throws CardException, MerchantException, AccountException, TransactionException {
        logger.info("Card withdraw method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);

        return ResponseEntity.ok(cardService.withdraw(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId()));
    }
}