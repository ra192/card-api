package com.card.service;

import com.card.entity.Card;
import com.card.entity.Transaction;
import com.card.entity.enums.CardType;
import com.card.entity.enums.TransactionType;
import com.card.repository.CardRepository;
import com.card.service.exception.AccountException;
import com.card.service.exception.CardException;
import com.card.service.exception.CustomerException;
import com.card.service.exception.TransactionException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    public CardService(CardRepository cardRepository, AccountService accountService, CustomerService customerService, TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.accountService = accountService;
        this.customerService = customerService;
        this.transactionService = transactionService;
    }

    public Card createVirtual(Long customerId, Long accountId) throws AccountException, CustomerException {

        final var account = accountService.findActiveById(accountId);
        final var customer = customerService.findActiveById(customerId);

        final var card = new Card();
        card.setType(CardType.VIRTUAL);
        card.setProviderReferenceId("xxxx");
        card.setCustomer(customer);
        card.setAccount(account);
        card.setCreated(LocalDateTime.now());
        card.setInfo("xxxx");

        cardRepository.save(card);

        return card;
    }

    public Transaction deposit(Long cardId, Long amount, String orderId) throws CardException, TransactionException {
        final var card = findById(cardId);
        return transactionService.withdraw(card.getAccount(), amount, TransactionType.VIRTUAL_CARD_DEPOSIT, orderId, card);
    }

    public Card findById(Long id) throws CardException {
        return cardRepository.findById(id).orElseThrow(()->new CardException("Card does not exist"));
    }

    public Transaction withdraw(Long cardId, Long amount, String orderId) throws CardException {
        final var card = findById(cardId);
        return transactionService.deposit(card.getAccount(), amount, TransactionType.VIRTUAL_CARD_WITHDRAW, orderId, card);
    }
}
