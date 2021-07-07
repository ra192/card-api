package com.card.service;

import com.card.entity.Customer;
import com.card.repository.CustomerRepository;
import com.card.service.dto.CreateCustomerDto;
import com.card.service.dto.CustomerDto;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final MerchantService merchantService;

    public CustomerService(CustomerRepository customerRepository,
                           MerchantService merchantService) {
        this.customerRepository = customerRepository;
        this.merchantService = merchantService;
    }

    public CustomerDto create(CreateCustomerDto createCustomerDto) throws CustomerException, MerchantException {
        logger.info("Create customer method was called with args:");
        logger.info(createCustomerDto.toString());

        final var merchant = merchantService.getById(createCustomerDto.getMerchantId());

        if (customerRepository.findByPhoneAndMerchant(createCustomerDto.getPhone(), merchant).isPresent())
            throw new CustomerException("Customer already exists");

        final var customer = new Customer();
        BeanUtils.copyProperties(createCustomerDto, customer);
        customer.setActive(true);
        customer.setMerchant(merchant);

        final var dbCustomer = customerRepository.save(customer);
        final var customerDto = new CustomerDto();
        BeanUtils.copyProperties(dbCustomer,customerDto);
        return customerDto;
    }

    public Customer findActiveById(Long id) throws CustomerException {
        return customerRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new CustomerException("Customer does not exist or is not active"));
    }
}
