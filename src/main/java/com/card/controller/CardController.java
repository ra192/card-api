package com.card.controller;

import com.card.entity.Card;
import com.card.entity.Transaction;
import com.card.service.CardService;
import com.card.service.dto.CardTransactionDto;
import com.card.service.dto.CreateCardDto;
import com.card.service.exception.AccountException;
import com.card.service.exception.CardException;
import com.card.service.exception.CustomerException;
import com.card.service.exception.TransactionException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card")
public class CardController extends BaseController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/issue/virtual")
    public Card createVirtual(@RequestBody CreateCardDto createDto) throws CustomerException, AccountException {
        return cardService.createVirtual(createDto);
    }

    @GetMapping("/deposit")
    public Transaction deposit(@RequestBody CardTransactionDto transactionDto) throws TransactionException, CardException {
        return cardService.deposit(transactionDto);
    }

    @GetMapping("/withdraw")
    public Transaction withdraw(@RequestBody CardTransactionDto transactionDto) throws CardException {
        return cardService.withdraw(transactionDto);
    }
}