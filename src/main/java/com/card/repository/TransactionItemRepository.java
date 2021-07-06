package com.card.repository;

import com.card.entity.Account;
import com.card.entity.TransactionItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionItemRepository extends CrudRepository<TransactionItem, Long> {
    @Query("select sum (itm.amount) from TransactionItem itm where itm.account = ?1")
    Long sumByAccount(Account account);
}
