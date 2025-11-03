package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartProductDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.ItemCart;
import com.codewithmosh.store.entities.Product;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-03T21:48:18+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public Cart toEntity(CartDto cartDto) {
        if ( cartDto == null ) {
            return null;
        }

        Cart cart = new Cart();

        cart.setId( cartDto.getId() );

        return cart;
    }

    @Override
    public CartDto toDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        Set<ItemCartDto> items = null;
        UUID id = null;

        items = itemCartSetToItemCartDtoSet( cart.getItemCart() );
        id = cart.getId();

        BigDecimal totalPrice = cart.getTotalPrice();
        Date createdAt = null;

        CartDto cartDto = new CartDto( id, createdAt, totalPrice, items );

        return cartDto;
    }

    @Override
    public ItemCartDto toDto(ItemCart itemCart) {
        if ( itemCart == null ) {
            return null;
        }

        ItemCartDto itemCartDto = new ItemCartDto();

        itemCartDto.setQuantity( itemCart.getQuantity() );

        itemCartDto.setTotalPrice( itemCart.getTotalPrice() );
        itemCartDto.setProduct( itemCart.getProduct() != null ? toDto(itemCart.getProduct()) : null );

        return itemCartDto;
    }

    @Override
    public CartProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        Long productId = null;
        String name = null;
        String description = null;

        productId = product.getId();
        name = product.getName();
        description = product.getDescription();

        CartProductDto cartProductDto = new CartProductDto( productId, name, description );

        return cartProductDto;
    }

    protected Set<ItemCartDto> itemCartSetToItemCartDtoSet(Set<ItemCart> set) {
        if ( set == null ) {
            return null;
        }

        Set<ItemCartDto> set1 = LinkedHashSet.newLinkedHashSet( set.size() );
        for ( ItemCart itemCart : set ) {
            set1.add( toDto( itemCart ) );
        }

        return set1;
    }
}
