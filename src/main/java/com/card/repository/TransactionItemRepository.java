package com.card.repository;

import com.card.entity.Account;
import com.card.entity.TransactionItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionItemRepository extends CrudRepository<TransactionItem, Long> {
    @Query("select sum (itm.amount) from TransactionItem itm where itm.srcAccount = ?1")
    Optional<Long> sumBySrcAccount(Account account);

    @Query("select sum (itm.amount) from TransactionItem itm where itm.destAccount = ?1")
    Optional<Long> sumByDestAccount(Account account);
}
