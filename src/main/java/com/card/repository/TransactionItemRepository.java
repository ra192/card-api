package com.card.repository;

import com.card.entity.Account;
import com.card.entity.TransactionItem;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionItemRepository extends CrudRepository<TransactionItem, Long> {
    Optional<Long> findSumAmountByAccount(Account account);
}
