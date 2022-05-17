package com.card.service.dto;

import com.card.entity.enums.CardType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CardDTO {
    private Long id;
    private CardType type;
    private LocalDateTime created;
    private String info;

    @NotNull(message = "account id is required")
    private Long accountId;

    @NotNull(message = "customer id is required")
    private Long customerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
