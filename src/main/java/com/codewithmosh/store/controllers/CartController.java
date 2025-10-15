package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.*;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.ItemCart;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ItemCartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Data
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final ItemCartRepository itemCartRepository;
    private final ProductRepository productRepository;
    
    @PostMapping
    public ResponseEntity<CartDto> createCart(){
      Cart cart = new Cart();
      cart.setDateCreated(new Date());
      cartRepository.save(cart);
      var cartDto =  cartMapper.toDto(cart);
      return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/{id}/item")
    public ResponseEntity<ItemCartDto> addToCart(
            @PathVariable UUID  id,
            @RequestBody AddItemToCartReqeustDto data
            ) {
        var cart = cartRepository.findById(id).orElse(null);
        var product = productRepository.findById(data.getProductId()).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build();
        if (product == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        var itemCart = cart.addItemCart(product);
        cartRepository.save(cart);
        var cartItemCartDto = cartMapper.toDto(itemCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable UUID id
    ){
        var cart = cartRepository.getCartWithItemsAndProducts(id);
        if(cart == null)
            return ResponseEntity.notFound().build();
        var cartDto = cartMapper.toDto(cart);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{id}/items/{idProduct}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable UUID id,
            @PathVariable Long idProduct,
           @Valid @RequestBody CartProductUpdateDto data
    ){
        var cart = cartRepository.getCartWithItemsAndProducts(id);
        if (cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Cart not found")
            );}
        var product = productRepository.findById(idProduct).orElse(null);
        if (product == null ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Product not found")
            );}
        var itemCart = cart.getItemCart(idProduct);
        if (itemCart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Item was not found in the cart")
            );}

        itemCart.setQuantity(data.getQuantity());
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.toDto(itemCart));

    }

}