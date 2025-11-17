package com.codewithmosh.store.order;

import com.codewithmosh.store.carts.ItemCartDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface Order_itemsMapper {
    Order_items toEntity(AddItemToOrderDto data);
    @Mapping(target = "product" , source = "product" )
    ItemCartDto toDto(Order_items order_items);
}
