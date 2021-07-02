package com.card.service;

import com.card.entity.Customer;
import com.card.repository.CustomerRepository;
import com.card.service.dto.CustomerDto;
import com.card.service.exception.AccountException;
import com.card.service.exception.CustomerException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    public CustomerService(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    public Customer create(CustomerDto customerDto) throws CustomerException, AccountException {
        final var account = accountService.findActiveById(customerDto.getAccountId());

        if (customerRepository.findByPhoneAndMerchant(customerDto.getPhone(), account.getMerchant()).isPresent())
            throw new CustomerException("Customer already exists");

        final var customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        customer.setActive(true);
        customer.setMerchant(account.getMerchant());

        return customerRepository.save(customer);
    }

    public Customer findActiveById(Long id) throws CustomerException {
        return customerRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new CustomerException("Customer does not exist or is not active"));
    }
}
