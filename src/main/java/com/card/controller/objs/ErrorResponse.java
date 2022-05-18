package com.card.controller.objs;

public class ErrorResponse {
    private final String errorText;

    public ErrorResponse(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }
}
