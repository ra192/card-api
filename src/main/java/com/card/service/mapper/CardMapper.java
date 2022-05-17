package com.card.service.mapper;

import com.card.service.dto.CardDTO;
import com.card.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDTO, Card> {
    @Override
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "customer.id", target = "customerId")
    CardDTO toDto(Card entity);
}
