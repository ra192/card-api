package com.card.controller;

import com.card.service.dto.CustomerDTO;
import com.card.service.CustomerService;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

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
    public CustomerDTO create(@RequestHeader String authorization, @RequestBody @Valid CustomerDTO customerDto) throws MerchantException, CustomerException {
        logger.info("Create customer method wath called with params:");
        logger.info(customerDto.toString());

        final var merchant = validateToken(authorization);
        if(!Objects.equals(merchant.getId(), customerDto.getMerchantId()))
            throw new MerchantException("Merchant id doesn't match with token");

        return customerService.create(customerDto);
    }
}