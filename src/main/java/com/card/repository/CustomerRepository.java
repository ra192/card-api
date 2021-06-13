package com.card.repository;

import com.card.entity.Customer;
import com.card.entity.Merchant;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByPhoneAndMerchant(String phone, Merchant merchant);

    Optional<Customer> findByIdAndActive(Long id, boolean active);
}
