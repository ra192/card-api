package com.card.controller;

import com.card.entity.Merchant;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.exception.TokenException;
import com.card.service.exception.MerchantException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public abstract class WithAuthMerchantController extends BaseController {
    private final TokenService tokenService;
    private final MerchantService merchantService;

    public WithAuthMerchantController(TokenService tokenService, MerchantService merchantService) {
        this.tokenService = tokenService;
        this.merchantService = merchantService;
    }

    protected Merchant validateToken(String authHeader) throws TokenException, MerchantException {
        final var token = StringUtils.replace(authHeader, "Bearer", "").trim();
        return merchantService.getById(tokenService.validateToken(token).getMerchantId());
    }
}
