package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartProductDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.ItemCart;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toEntity(CartDto cartDto);

    @Mapping(target = "items" , source = "itemCart")
    @Mapping(target = "totalPrice" , expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);
    @Mapping(target = "totalPrice", expression = "java(itemCart.getTotalPrice())")
    @Mapping(target = "product", expression = "java(itemCart.getProduct() != null ? toDto(itemCart.getProduct()) : null)")
    ItemCartDto toDto(ItemCart itemCart);
    @Mapping(target = "productId" , source = "id")
    CartProductDto toDto(Product product);

}