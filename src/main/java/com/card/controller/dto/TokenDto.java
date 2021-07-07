package com.card.controller.dto;

import java.time.LocalDateTime;

public class TokenDto {
    private final String token;
    private final LocalDateTime expiredAt;

    public TokenDto(String token, LocalDateTime expiredAt) {
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
