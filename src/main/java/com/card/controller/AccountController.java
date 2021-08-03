package com.card.controller;

import com.card.controller.dto.TopupDto;
import com.card.controller.dto.TransactionDto;
import com.card.service.AccountService;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.exception.AccountException;
import com.card.service.exception.MerchantException;
import com.card.service.exception.TransactionException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController extends WithAuthMerchantController {
    private static final Long INTERNAL_MERCHANT_ID = 1L;
    private final AccountService accountService;

    public AccountController(TokenService tokenService, MerchantService merchantService, AccountService accountService) {
        super(tokenService, merchantService);
        this.accountService = accountService;
    }

    @PostMapping("/topup")
    public TransactionDto topup(@RequestHeader String authorization, @RequestBody TopupDto requestObject)
            throws MerchantException, AccountException, TransactionException {
        final var merchant = validateToken(authorization);
        if (!merchant.getId().equals(INTERNAL_MERCHANT_ID)) throw new MerchantException("Internal merchant required");

        final var transaction = accountService.topup(accountService.findActiveById(requestObject.getAccountId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }
}
