package com.card.service;

import com.card.entity.Card;
import com.card.entity.Transaction;
import com.card.entity.enums.CardType;
import com.card.entity.enums.TransactionType;
import com.card.repository.CardRepository;
import com.card.service.dto.CardDto;
import com.card.service.dto.CreateCardTransactionDto;
import com.card.service.dto.CreateCardDto;
import com.card.service.dto.TransactionDto;
import com.card.service.exception.AccountException;
import com.card.service.exception.CardException;
import com.card.service.exception.CustomerException;
import com.card.service.exception.TransactionException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public CardDto createVirtual(CreateCardDto createDto) throws AccountException, CustomerException {

        final var account = accountService.findActiveById(createDto.getAccountId());
        final var customer = customerService.findActiveById(createDto.getCustomerId());

        final var card = new Card();
        card.setType(CardType.VIRTUAL);
        card.setProviderReferenceId("xxxx");
        card.setCustomer(customer);
        card.setAccount(account);
        card.setCreated(LocalDateTime.now());
        card.setInfo("xxxx");

        cardRepository.save(card);

        final var cardDto = new CardDto();
        BeanUtils.copyProperties(card,cardDto);

        return cardDto;
    }

    public TransactionDto deposit(CreateCardTransactionDto createDto) throws CardException, TransactionException {
        final var card = findById(createDto.getCardId());
        final Transaction transaction = transactionService.withdraw(card.getAccount(), createDto.getAmount(),
                TransactionType.VIRTUAL_CARD_DEPOSIT, createDto.getOrderId(), card);
        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }

    public Card findById(Long id) throws CardException {
        return cardRepository.findById(id).orElseThrow(()->new CardException("Card does not exist"));
    }

    public TransactionDto withdraw(CreateCardTransactionDto createDto) throws CardException {
        final var card = findById(createDto.getCardId());
        final Transaction transaction = transactionService.deposit(card.getAccount(), createDto.getAmount(),
                TransactionType.VIRTUAL_CARD_WITHDRAW, createDto.getOrderId(), card);
        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }
}
