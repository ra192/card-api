package com.card.service.mapper;

import com.card.entity.Transaction;
import com.card.service.dto.TransactionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
}
