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
import com.card.service.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final TransactionFeeRepository transactionFeeRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionItemRepository transactionItemRepository,
                              TransactionFeeRepository transactionFeeRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionItemRepository = transactionItemRepository;
        this.transactionFeeRepository = transactionFeeRepository;
    }

    public Transaction deposit(Account srcAccount, Account destAccount, Account feeAccount, Long amount,
                               TransactionType type, String orderId, Card card) throws TransactionException {
        final var feeAmount = calculateFeeAmount(amount, type, destAccount);
        if (sumByAccount(srcAccount) - amount < 0)
            throw new TransactionException("Source account does not have enough funds");

        final var transaction = createTransaction(srcAccount, destAccount, amount, type, orderId, card);
        if (feeAmount > 0)
            transactionItemRepository.save(new TransactionItem(transaction, feeAmount, destAccount, feeAccount, null));

        return transaction;
    }

    public Transaction topup(Account srcAccount, Account destAccount, Long amount, TransactionType type, String orderId) throws TransactionException {
        return createTransaction(srcAccount, destAccount, amount, type, orderId, null);
    }

    public Transaction withdraw(Account srcAccount, Account destAccount, Account feeAccount, Long amount,
                                TransactionType type, String orderId, Card card) throws TransactionException {
        final var feeAmount = calculateFeeAmount(amount, type, srcAccount);
        if (sumByAccount(srcAccount) - amount - feeAmount < 0)
            throw new TransactionException("Source account does not have enough funds");

        final var transaction = createTransaction(srcAccount, destAccount, amount, type, orderId, card);
        if (feeAmount > 0)
            transactionItemRepository.save(new TransactionItem(transaction, feeAmount, srcAccount, feeAccount, null));

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

    private long sumByAccount(Account account) {
        return transactionItemRepository.findSumAmountByDestAccount(account).orElse(0L)
                - transactionItemRepository.findSumAmountBySrcAccount(account).orElse(0L);
    }

    private long calculateFeeAmount(Long amount, TransactionType type, Account account) {
        return transactionFeeRepository.findByTypeAndAccount(type, account)
                .map(fee -> amount * fee.getRate().longValue()).orElse(0L);
    }
}
