package com.card.repository;

import com.card.entity.TransactionItem;
import org.springframework.data.repository.CrudRepository;

public interface TransactionItemRepository extends CrudRepository<TransactionItem, Long> {
}
