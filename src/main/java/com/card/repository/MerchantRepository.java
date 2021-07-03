package com.card.repository;

import com.card.entity.Merchant;
import org.springframework.data.repository.CrudRepository;

public interface MerchantRepository extends CrudRepository<Merchant, Long> {
}
