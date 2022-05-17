package com.card.service.mapper;

import com.card.service.dto.CustomerDTO;
import com.card.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Override
    @Mapping(source = "merchant.id", target = "merchantId")
    CustomerDTO toDto(Customer entity);
}
