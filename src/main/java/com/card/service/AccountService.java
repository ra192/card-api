package com.card.service;

import com.card.entity.Account;
import com.card.repository.AccountRepository;
import com.card.service.exception.AccountException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private static final Long CASH_ACCOUNT_ID = 1L;

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findActiveById(Long id) throws AccountException {
        return accountRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new AccountException("Account does not exist"));
    }

    protected Account getCashAccount() throws AccountException {
        return findActiveById(CASH_ACCOUNT_ID);
    }
}
