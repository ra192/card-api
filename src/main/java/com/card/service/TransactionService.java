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

import java.util.ArrayList;
import java.util.Optional;

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
        final var feeItem = createFeeItem(destAccount,
                feeAccount, amount, type);
        if (sumByAccount(srcAccount) - amount < 0)
            throw new TransactionException("Source account does not have enough funds");

        return createTransaction(srcAccount, destAccount, amount, type, orderId, card, feeItem);
    }

    public Transaction topup(Account srcAccount, Account destAccount, Long amount, TransactionType type, String orderId) {
        return createTransaction(srcAccount,destAccount,amount,type,orderId,null, Optional.empty());
    }

    public Transaction withdraw(Account srcAccount, Account destAccount, Account feeAccount, Long amount,
                                TransactionType type, String orderId, Card card) throws TransactionException {
        final var feeItem = createFeeItem(srcAccount,
                feeAccount, amount, type);
        if (sumByAccount(srcAccount) - amount - feeItem.map(TransactionItem::getAmount).orElse(0L) < 0)
            throw new TransactionException("Source account does not have enough funds");
        return createTransaction(srcAccount, destAccount, amount, type, orderId, card, feeItem);
    }

    private Transaction createTransaction(Account srcAccount, Account destAccount, Long amount, TransactionType type,
                                          String orderId, Card card, Optional<TransactionItem> feeItem) {
        final var items = new ArrayList<TransactionItem>();
        items.add(new TransactionItem(amount, srcAccount, destAccount, card));

        feeItem.ifPresent(items::add);

        final var transaction = new Transaction(orderId, type, TransactionStatus.COMPLETED, items);
        transactionRepository.save(transaction);

        log.info("{} transactions was created", type);

        return transaction;
    }

    private Optional<TransactionItem> createFeeItem(Account srcAccount, Account destAccount, Long amount, TransactionType type) {
        return transactionFeeRepository.findByTypeAndAccount(type, srcAccount)
                .map(fee -> new TransactionItem(amount * fee.getRate().longValue(),
                        srcAccount, destAccount, null));
    }

    private long sumByAccount(Account account) {
        return transactionItemRepository.findSumAmountByDestAccount(account).orElse(0L)
                - transactionItemRepository.findSumAmountBySrcAccount(account).orElse(0L);
    }
}
