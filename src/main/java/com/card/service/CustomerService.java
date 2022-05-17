package com.card.service;

import com.card.service.dto.CustomerDTO;
import com.card.entity.Customer;
import com.card.entity.Merchant;
import com.card.service.mapper.CustomerMapper;
import com.card.repository.CustomerRepository;
import com.card.repository.MerchantRepository;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, MerchantRepository merchantRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDTO create(CustomerDTO customerDto) throws CustomerException, MerchantException {
        final Merchant merchant = merchantRepository.findById(customerDto.getMerchantId())
                .orElseThrow(() -> new MerchantException("Merchant doesn't exist"));

        if (customerRepository.findByPhoneAndMerchant(customerDto.getPhone(), merchant).isPresent())
            throw new CustomerException("Customer already exists");

        Customer customer = customerMapper.toEntity(customerDto);
        customer.setMerchant(merchant);
        customer.setActive(true);

        customer=customerRepository.save(customer);

        logger.info("Customer was created with id: {}", customer.getId());

        return customerMapper.toDto(customer);
    }

    public CustomerDTO findActiveById(Long id) throws CustomerException {
        return customerRepository.findByIdAndActive(id, true).map(customerMapper::toDto)
                .orElseThrow(() -> new CustomerException("Customer does not exist or is not active"));
    }
}
