package com.card.controller.dto;

@SuppressWarnings("unused")
public class BaseResultDto {
    private final String code;
    private final String errorText;

    public BaseResultDto(String code, String errorText) {
        this.code = code;
        this.errorText = errorText;
    }

    public String getCode() {
        return code;
    }

    public String getErrorText() {
        return errorText;
    }
}
