package com.card.service;

import com.card.entity.Account;
import com.card.entity.Card;
import com.card.entity.Transaction;
import com.card.entity.TransactionItem;
import com.card.entity.enums.TransactionStatus;
import com.card.entity.enums.TransactionType;
import com.card.repository.TransactionFeeRepository;
import com.card.repository.TransactionItemRepository;
import com.card.repository.TransactionRepository;
import com.card.service.exception.AccountException;
import com.card.service.exception.TransactionException;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final TransactionFeeRepository transactionFeeRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionItemRepository transactionItemRepository,
                              TransactionFeeRepository transactionFeeRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.transactionItemRepository = transactionItemRepository;
        this.transactionFeeRepository = transactionFeeRepository;
        this.accountService = accountService;
    }

    public Transaction deposit(Account srcAccount, Account destAccount, Account feeAccount, Long amount,
                               TransactionType type, String orderId, Card card) throws TransactionException {
        final var feeAmount = calculateFee(amount, type, destAccount);
        if (srcAccount.getBalance() - amount < 0)
            throw new TransactionException("Source account does not have enough funds");

        final var transaction = createTransaction(srcAccount, destAccount, amount, type, orderId, card);
        if (feeAmount > 0)
            transactionItemRepository.save(new TransactionItem(transaction, feeAmount, destAccount, feeAccount, null));

        updateBalance(srcAccount, -amount);
        updateBalance(destAccount, amount - feeAmount);
        if (feeAmount > 0) updateBalance(feeAccount, feeAmount);

        return transaction;
    }

    public Transaction fund(Account account, Long amount, String orderId) throws TransactionException, AccountException {
        final var cashAccount = accountService.getCashAccount();

        final var transaction = createTransaction(cashAccount, account, amount, TransactionType.FUND, orderId, null);

        updateBalance(cashAccount, -amount);
        updateBalance(account, amount);

        return transaction;
    }

    public Transaction withdraw(Account srcAccount, Account destAccount, Account feeAccount, Long amount,
                                TransactionType type, String orderId, Card card) throws TransactionException {
        final var feeAmount = calculateFee(amount, type, srcAccount);
        if (srcAccount.getBalance() - amount - feeAmount < 0)
            throw new TransactionException("Source account does not have enough funds");

        final var transaction = createTransaction(srcAccount, destAccount, amount, type, orderId, card);
        if (feeAmount > 0)
            transactionItemRepository.save(new TransactionItem(transaction, feeAmount, srcAccount, feeAccount, null));

        updateBalance(srcAccount, -amount-feeAmount);
        updateBalance(destAccount, amount);
        if (feeAmount > 0) updateBalance(feeAccount, feeAmount);

        return transaction;
    }

    private Transaction createTransaction(Account srcAccount, Account destAccount, Long amount, TransactionType type,
                                          String orderId, Card card) throws TransactionException {
        if (!srcAccount.getCurrency().equals(destAccount.getCurrency()))
            throw new TransactionException("Source account currency doesn't match destination account currency");

        final var transaction = transactionRepository
                .save(new Transaction(orderId, type, TransactionStatus.COMPLETED));

        transactionItemRepository.save(new TransactionItem(transaction, amount, srcAccount, destAccount, card));

        return transaction;
    }

    private long calculateFee(Long amount, TransactionType type, Account account) {
        return transactionFeeRepository.findByTypeAndAccount(type, account)
                .map(fee -> amount * fee.getRate().longValue()).orElse(0L);
    }

    private void updateBalance(Account srcAccount, Long amount) {
        srcAccount.setBalance(srcAccount.getBalance() + amount);
        accountService.save(srcAccount);
    }
}
