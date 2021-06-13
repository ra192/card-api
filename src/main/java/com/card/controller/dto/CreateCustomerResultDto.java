package com.card.controller.dto;

@SuppressWarnings("unused")
public class CreateCustomerResultDto extends BaseResultDto {
    private final Long customerId;

    public CreateCustomerResultDto(String code, String errorText, Long customerId) {
        super(code, errorText);
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
