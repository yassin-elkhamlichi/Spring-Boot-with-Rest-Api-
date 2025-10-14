package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.entities.Cart;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toCart(CartDto cartDto);
    CartDto toCartDto(Cart cart);

}
