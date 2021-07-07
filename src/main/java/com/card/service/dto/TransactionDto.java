package com.card.service.dto;

public class TransactionDto {
    private final Long id;
    private final String status;

    public TransactionDto(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
