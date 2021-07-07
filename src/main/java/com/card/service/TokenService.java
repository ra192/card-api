package com.card.service;

import com.card.entity.Merchant;
import com.card.service.data.Token;
import com.card.service.exception.TokenException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class TokenService {
    private final Cache cache;

    private final int tokenSize;
    private final long tokenLifetimeInMinutes;

    public TokenService(CacheManager cacheManager,
                        @Value("${com.card.auth.token.size}") int tokenSize,
                        @Value("${com.card.auth.liftime.minutes}") long tokenLifetimeInMinutes) {
        this.cache = cacheManager.getCache("tokens");
        this.tokenSize = tokenSize;
        this.tokenLifetimeInMinutes = tokenLifetimeInMinutes;
    }

    public Token createToken(Merchant merchant, String secret) throws NoSuchAlgorithmException, TokenException {
        if (!merchant.getSecret().equalsIgnoreCase(sha256Hash(secret)))
            throw new TokenException("Secret is not valid");

        final var tokenStr = RandomStringUtils.randomAlphanumeric(tokenSize);
        final var expiredAt = LocalDateTime.now().plusMinutes(tokenLifetimeInMinutes);

        final var token = new Token(tokenStr, merchant.getId(), LocalDateTime.now().plusMinutes(tokenLifetimeInMinutes));
        cache.put(tokenStr, token);

        return token;
    }

    public Token validateToken(String token) throws TokenException {
        final var cacheValue = cache.get(token);
        if (cacheValue == null) throw new TokenException("Token doesn't exist");
        final var tokenObj = (Token) cacheValue.get();

        assert tokenObj != null;
        if (LocalDateTime.now().isAfter(tokenObj.getExpiredAt())) throw new TokenException("Token is expired");

        return tokenObj;
    }

    private String sha256Hash(String text) throws NoSuchAlgorithmException {
        final var digest = MessageDigest.getInstance("SHA-256");
        return new String(Base64.getEncoder().encode(digest.digest(text.getBytes(StandardCharsets.UTF_8))),
                StandardCharsets.UTF_8);
    }
}
