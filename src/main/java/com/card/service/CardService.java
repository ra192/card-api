package com.card.service;

import com.card.service.dto.CardDTO;
import com.card.entity.Account;
import com.card.entity.Card;
import com.card.entity.Customer;
import com.card.entity.Transaction;
import com.card.entity.enums.TransactionType;
import com.card.service.mapper.CardMapper;
import com.card.repository.CardRepository;
import com.card.repository.CustomerRepository;
import com.card.service.exception.AccountException;
import com.card.service.exception.CardException;
import com.card.service.exception.CustomerException;
import com.card.service.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardService {
    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    private static final Long CARD_ACCOUNT_ID = 2L;
    private static final Long FEE_ACCOUNT_ID = 3L;

    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final TransactionService transactionService;

    private final CardMapper cardMapper;

    public CardService(CardRepository cardRepository, CustomerRepository customerRepository, AccountService accountService, TransactionService transactionService, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.customerRepository = customerRepository;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.cardMapper = cardMapper;
    }

    public CardDTO create(CardDTO cardDTO) throws AccountException, CustomerException {
        final Account account = accountService.findActiveById(cardDTO.getAccountId());
        final Customer customer = customerRepository.findByIdAndActive(cardDTO.getCustomerId(), true)
                .orElseThrow(() -> new CustomerException("Customer with specified id doesn't exist"));

        Card card = cardMapper.toEntity(cardDTO);
        card.setAccount(account);
        card.setCustomer(customer);
        card.setProviderReferenceId("xxxx");
        card.setInfo("xxxx");
        card.setCreated(LocalDateTime.now());

        card = cardRepository.save(card);
        logger.info("Card was created with id: {}", card.getId());

        return cardMapper.toDto(card);
    }

    public Transaction deposit(Card card, Long amount, String orderId) throws TransactionException, AccountException {
        return transactionService.withdraw(card.getAccount(), accountService.findActiveById(CARD_ACCOUNT_ID),
                accountService.findActiveById(FEE_ACCOUNT_ID), amount,
                TransactionType.VIRTUAL_CARD_DEPOSIT, orderId, card);
    }

    public Card findById(Long id) throws CardException {
        return cardRepository.findById(id).orElseThrow(() -> new CardException("Card does not exist"));
    }

    public Transaction withdraw(Card card, Long amount, String orderId) throws AccountException, TransactionException {
        return transactionService.deposit(accountService.findActiveById(CARD_ACCOUNT_ID), card.getAccount(),
                accountService.findActiveById(FEE_ACCOUNT_ID), amount,
                TransactionType.VIRTUAL_CARD_WITHDRAW, orderId, card);
    }
}
