package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.*;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exception.CartNotFoundException;
import com.codewithmosh.store.exception.ItemNotFoundException;
import com.codewithmosh.store.exception.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ItemCartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.services.CartService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Data
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final ItemCartRepository itemCartRepository;
    private final ProductRepository productRepository;
    private final CartService cartService ;

    @PostMapping
    public ResponseEntity<CartDto> createCart(){
     var cartDto = cartService.createCart();
      return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/{id}/item")
    public ResponseEntity<ItemCartDto> addToCart(
            @PathVariable UUID  id,
            @RequestBody AddItemToCartReqeustDto data
            ) {
        var cartItemCartDto = cartService.addToCart(id, data.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable UUID id
    ){
        var cartDto = cartService.getCart(id);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{id}/items/{idProduct}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable UUID id,
            @PathVariable Long idProduct,
           @Valid @RequestBody CartProductUpdateDto data
    ){
        var itemCart = cartService.updateCartItem(id, idProduct, data);
        return ResponseEntity.ok(cartMapper.toDto(itemCart));
    }

    @DeleteMapping("/{id}/items/{idProduct}")
    public ResponseEntity<?> deleteItemInCart(
            @PathVariable("id") UUID id,
            @PathVariable("idProduct") Long idProduct
    ){
        cartService.deleteProductInCart(id, idProduct);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/items")
    public ResponseEntity<?> clearCart(
            @PathVariable("id") UUID id
    ){
       cartService.clearCart(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error" , "Cart not found")
        );
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error" , "Product not found")
        );
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleItemException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error" , "Item not found in the cart")
        );
    }

}