package com.card.service.dto;

import com.card.entity.enums.TransactionStatus;
import com.card.entity.enums.TransactionType;

public class TransactionDTO {
    private Long id;
    private String orderId;
    private TransactionType type;
    private TransactionStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
