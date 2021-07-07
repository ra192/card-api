package com.card.controller;

import com.card.entity.Customer;
import com.card.service.CustomerService;
import com.card.service.dto.CreateCustomerDto;
import com.card.service.dto.CustomerDto;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Register customer")
    @PostMapping("/register")
    public CustomerDto register(@RequestBody @Valid CreateCustomerDto createCustomerDto) throws MerchantException, CustomerException {
        return customerService.create(createCustomerDto);
    }
}
