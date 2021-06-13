package com.card.repository;

import com.card.entity.Account;
import com.card.entity.TransactionFee;
import com.card.entity.enums.TransactionType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionFeeRepository extends CrudRepository<TransactionFee, Long> {
    Optional<TransactionFee>findByTypeAndAccount(TransactionType type, Account account);
}
