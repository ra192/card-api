package com.card.controller;

import com.card.entity.Merchant;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.exception.MerchantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public abstract class WithAuthMerchantController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WithAuthMerchantController.class);

    private final TokenService tokenService;
    private final MerchantService merchantService;

    public WithAuthMerchantController(TokenService tokenService, MerchantService merchantService) {
        this.tokenService = tokenService;
        this.merchantService = merchantService;
    }

    protected Merchant validateToken(String authHeader) throws MerchantException {
        logger.info("Authorization header: {}", authHeader);
        final var token = StringUtils.replace(authHeader, "Bearer", "").trim();
        return merchantService.findById(tokenService.validate(token));
    }
}
