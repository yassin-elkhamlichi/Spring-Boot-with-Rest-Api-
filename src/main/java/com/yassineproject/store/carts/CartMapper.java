package com.yassineproject.store.carts;

import com.yassineproject.store.products.Product;
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