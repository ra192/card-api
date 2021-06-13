package com.card.controller.dto;

@SuppressWarnings("unused")
public class TransactionResultDto extends BaseResultDto {
    private final Long transactionId;

    public TransactionResultDto(String code, String errorText, Long transactionId) {
        super(code, errorText);
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return transactionId;
    }
}
