package com.card.controller.dto;

@SuppressWarnings("unused")
public class CreateCardResultDto extends BaseResultDto {
    private final Long cardId;

    public CreateCardResultDto(String code, String errorText, Long cardId) {
        super(code, errorText);
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
    }
}
