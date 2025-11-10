package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.AddItemToOrderDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.entities.Order_items;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface Order_itemsMapper {
    Order_items toEntity(AddItemToOrderDto data);
    @Mapping(target = "product" , source = "product" )
    ItemCartDto toDto(Order_items order_items);
}
