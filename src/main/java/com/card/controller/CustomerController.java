package com.card.controller;

import com.card.controller.dto.CreateCustomerResultDto;
import com.card.service.CustomerService;
import com.card.service.dto.CustomerDto;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public CreateCustomerResultDto register(@RequestBody @Valid CustomerDto customerDto) {
        log.info("Register customer method was called. Request body:");
        log.info(customerDto.toString());
        try {
            final var customer = customerService.create(customerDto);
            return new CreateCustomerResultDto("ok", "", customer.getId());
        } catch (CustomerException | MerchantException e) {
            log.error(e.getMessage());
            return new CreateCustomerResultDto("error", e.getMessage(), 0L);
        }
    }
}
