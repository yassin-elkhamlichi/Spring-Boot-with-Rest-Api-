package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.AddItemToOrderDto;
import com.codewithmosh.store.dtos.CartProductDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.entities.Order_items;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Order_itemsMapper {
    Order_items toEntity(AddItemToOrderDto data);
    ItemCartDto toDto(Order_items order_items);
}
