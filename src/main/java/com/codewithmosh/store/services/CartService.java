package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartProductUpdateDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.ItemCart;
import com.codewithmosh.store.exception.CartNotFoundException;
import com.codewithmosh.store.exception.ItemNotFoundException;
import com.codewithmosh.store.exception.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@Getter
@Setter
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        Cart cart = new Cart();
        cart.setDateCreated(new Date());
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public ItemCartDto addToCart(UUID id, Long productId) {
        var cart = cartRepository.findById(id).orElse(null);
        var product = productRepository.findById(productId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
        if (product == null)
            throw new ProductNotFoundException();
        var itemCart = cart.addItemCart(product);
        cartRepository.save(cart);
        return cartMapper.toDto(itemCart);
    }
    public CartDto getCart(UUID id) {
        var cart = cartRepository.getCartWithItemsAndProducts(id);
        if(cart == null)
            throw new CartNotFoundException();
        return cartMapper.toDto(cart);
    }
    public ItemCart updateCartItem(UUID id, Long idProduct, CartProductUpdateDto data) {
        var cart = cartRepository.getCartWithItemsAndProducts(id);
        if (cart == null){
            throw new CartNotFoundException();
        }
        var product = productRepository.findById(idProduct).orElse(null);
        if (product == null ){
            throw new ProductNotFoundException();
        }
        var itemCart = cart.getItemCart(idProduct);
        if (itemCart == null){
            throw new ItemNotFoundException();
        }
        itemCart.setQuantity(data.getQuantity());
        cartRepository.save(cart);
        return itemCart;
    }

    public void deleteProductInCart(UUID id, Long idProduct) {
        var cart = cartRepository.getCartWithItemsAndProducts(id);
        if(cart == null)
            throw new CartNotFoundException();
        cart.removeItemCart(idProduct);
        cartRepository.save(cart);
    }

    public void clearCart(UUID id) {
        var cart = cartRepository.getCartWithItemsAndProducts(id);
        if(cart == null)
            throw new CartNotFoundException();
        cart.clear();
        cartRepository.save(cart);
    }
}
