package com.card.controller;

public class ErrorResult {
    private final String errorText;

    public ErrorResult(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }
}
