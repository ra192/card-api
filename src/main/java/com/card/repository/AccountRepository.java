package com.card.repository;

import com.card.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account>findByIdAndActive(Long id, Boolean active);
}
