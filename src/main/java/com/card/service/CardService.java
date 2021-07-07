package com.card.service;

import com.card.entity.Card;
import com.card.entity.Transaction;
import com.card.entity.enums.TransactionType;
import com.card.repository.CardRepository;
import com.card.controller.dto.CardDto;
import com.card.controller.dto.CreateCardTransactionDto;
import com.card.controller.dto.CreateCardDto;
import com.card.controller.dto.TransactionDto;
import com.card.service.exception.CardException;
import com.card.service.exception.TransactionException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final TransactionService transactionService;

    public CardService(CardRepository cardRepository, TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.transactionService = transactionService;
    }

    public void create(Card card) {
        card.setProviderReferenceId("xxxx");
        card.setInfo("xxxx");
        card.setCreated(LocalDateTime.now());
        cardRepository.save(card);
    }

    public Transaction deposit(Card card, Long amount, String orderId) throws CardException, TransactionException {
        return transactionService.withdraw(card.getAccount(),amount,
                TransactionType.VIRTUAL_CARD_DEPOSIT, orderId, card);
    }

    public Card findById(Long id) throws CardException {
        return cardRepository.findById(id).orElseThrow(()->new CardException("Card does not exist"));
    }

    public Transaction withdraw(Card card, Long amount, String orderId) throws CardException {
       return transactionService.deposit(card.getAccount(), amount,
                TransactionType.VIRTUAL_CARD_WITHDRAW, orderId, card);
    }
}
