package com.card.controller;

import com.card.controller.objs.CreateTokenRequest;
import com.card.service.dto.TokenDTO;
import com.card.service.TokenService;
import com.card.service.MerchantService;
import com.card.service.exception.TokenException;
import com.card.service.exception.MerchantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/token")
public class TokenController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> create(@RequestBody CreateTokenRequest requestObj) throws MerchantException,
            NoSuchAlgorithmException, TokenException {
        logger.info("Create token method was called with params:");
        logger.info(requestObj.toString());

        return ResponseEntity.ok(tokenService.create(requestObj.getMerchantId(), requestObj.getSecret()));
    }
}
