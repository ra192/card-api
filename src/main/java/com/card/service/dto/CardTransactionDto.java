package com.card.service.dto;

import javax.validation.constraints.NotNull;

public class CardTransactionDto {
    @NotNull(message = "card id is required")
    private Long cardId;
    @NotNull(message = "amount is required")
    private Long amount;
    @NotNull(message = "order id is required")
    private String orderId;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
