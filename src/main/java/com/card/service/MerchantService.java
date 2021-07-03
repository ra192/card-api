package com.card.service;

import com.card.entity.Merchant;
import com.card.repository.MerchantRepository;
import com.card.service.exception.MerchantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
    private final MerchantRepository merchantRepository;

    @Autowired
    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant getById(Long id) throws MerchantException {
        return merchantRepository.findById(id).orElseThrow(()->new MerchantException("Merchant does not exist"));
    }
}
