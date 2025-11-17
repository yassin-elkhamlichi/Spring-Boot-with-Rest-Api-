package com.codewithmosh.store.order;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Orders order);
}
