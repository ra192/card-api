package com.card.service;

import com.card.entity.Customer;
import com.card.repository.CustomerRepository;
import com.card.service.dto.CustomerDto;
import com.card.service.exception.AccountException;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final MerchantService merchantService;

    public CustomerService(CustomerRepository customerRepository,
                           MerchantService merchantService) {
        this.customerRepository = customerRepository;
        this.merchantService = merchantService;
    }

    public Customer create(CustomerDto customerDto) throws CustomerException, MerchantException {
        final var merchant = merchantService.getById(customerDto.getMerchantId());

        if (customerRepository.findByPhoneAndMerchant(customerDto.getPhone(), merchant).isPresent())
            throw new CustomerException("Customer already exists");

        final var customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        customer.setActive(true);
        customer.setMerchant(merchant);

        return customerRepository.save(customer);
    }

    public Customer findActiveById(Long id) throws CustomerException {
        return customerRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new CustomerException("Customer does not exist or is not active"));
    }
}
