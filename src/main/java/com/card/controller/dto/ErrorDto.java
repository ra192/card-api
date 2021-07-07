package com.card.controller.dto;

public class ErrorDto {
    private final String errorText;

    public ErrorDto(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }
}
