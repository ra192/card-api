package com.card.controller;

import com.card.controller.dto.FundAccountDto;
import com.card.service.dto.TransactionDTO;
import com.card.service.AccountService;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.TransactionService;
import com.card.service.exception.AccountException;
import com.card.service.exception.MerchantException;
import com.card.service.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController extends WithAuthMerchantController {
    private static final Logger logger= LoggerFactory.getLogger(AccountController.class);

    private static final Long INTERNAL_MERCHANT_ID = 1L;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(TokenService tokenService, MerchantService merchantService, AccountService accountService, TransactionService transactionService) {
        super(tokenService, merchantService);
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping("/fund")
    public TransactionDTO fund(@RequestHeader String authorization, @RequestBody FundAccountDto requestObject)
            throws MerchantException, AccountException, TransactionException {
        logger.info("Fund account method was called with params:");
        logger.info(requestObject.toString());

        final var merchant = validateToken(authorization);
        if (!merchant.getId().equals(INTERNAL_MERCHANT_ID)) throw new MerchantException("Internal merchant required");

        return transactionService.fund(accountService.findActiveById(requestObject.getAccountId()),
                requestObject.getAmount(), requestObject.getOrderId());
    }
}
