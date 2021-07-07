package com.card.controller;

import com.card.entity.Customer;
import com.card.service.CustomerService;
import com.card.service.MerchantService;
import com.card.controller.dto.CreateCustomerDto;
import com.card.controller.dto.CustomerDto;
import com.card.service.TokenService;
import com.card.service.exception.TokenException;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController extends WithAuthMerchantController {
    private static final Logger logger= LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService, MerchantService merchantService,
                              TokenService tokenService) {
        super(tokenService, merchantService);
        this.customerService = customerService;
    }

    @Operation(summary = "Register customer")
    @PostMapping
    public CustomerDto create(@RequestHeader String authorization, @RequestBody @Valid CreateCustomerDto requestObject) throws MerchantException, CustomerException, TokenException {
        logger.info("Create customer method wath called with params:");
        logger.info(requestObject.toString());

        final var merchant = validateToken(authorization);

        final var customer = new Customer();
        BeanUtils.copyProperties(requestObject, customer);
        customer.setActive(true);
        customer.setMerchant(merchant);

        customerService.create(customer);

        final var result = new CustomerDto();
        BeanUtils.copyProperties(customer, result);

        return result;
    }
}