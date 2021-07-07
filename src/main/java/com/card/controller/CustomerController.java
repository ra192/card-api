package com.card.controller;

import com.card.entity.Customer;
import com.card.service.CustomerService;
import com.card.service.MerchantService;
import com.card.controller.dto.CreateCustomerDto;
import com.card.controller.dto.CustomerDto;
import com.card.service.exception.CustomerException;
import com.card.service.exception.MerchantException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController extends BaseController {

    private final CustomerService customerService;
    private final MerchantService merchantService;

    public CustomerController(CustomerService customerService, MerchantService merchantService) {
        this.customerService = customerService;
        this.merchantService = merchantService;
    }

    @Operation(summary = "Register customer")
    @PostMapping
    public CustomerDto create(@RequestBody @Valid CreateCustomerDto requestObject) throws MerchantException, CustomerException {

        final var customer = new Customer();
        BeanUtils.copyProperties(requestObject, customer);
        customer.setActive(true);
        customer.setMerchant(merchantService.getById(requestObject.getMerchantId()));

        customerService.create(customer);

        final var result = new CustomerDto();
        BeanUtils.copyProperties(customer,result);

        return result;
    }
}