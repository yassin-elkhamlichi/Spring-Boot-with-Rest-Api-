package com.codewithmosh.store.mappers;


import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.entities.Orders;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Orders order);
    Orders toEntity(OrderDto orderDto);
}
