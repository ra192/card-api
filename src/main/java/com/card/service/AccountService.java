package com.card.service;

import com.card.entity.Account;
import com.card.entity.Transaction;
import com.card.entity.enums.TransactionType;
import com.card.repository.AccountRepository;
import com.card.service.exception.AccountException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private static final Long CASH_ACCOUNT_ID = 1L;

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Transaction topup(Account account, Long amount, String orderId) throws AccountException {
        return transactionService.topup(getCashAccount(), account, amount, TransactionType.TOPUP, orderId);
    }

    public Account findActiveById(Long id) throws AccountException {
        return accountRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new AccountException("Account does not exist"));
    }

    private Account getCashAccount() throws AccountException {
        return findActiveById(CASH_ACCOUNT_ID);
    }
}
