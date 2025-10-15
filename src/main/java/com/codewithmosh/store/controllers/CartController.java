package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartReqeustDto;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.ItemCart;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ItemCartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        System.out.println(product.getId());
        var itemCart = new ItemCart();
        itemCart.setCart(cart);
        itemCart.setProduct(product);
        int totalQuantity = 1;
        if (!itemCartRepository.existsByProductIdAndCartId(data.getProductId(), id))
        {
            itemCart.setQuantity(totalQuantity);

        }else {
            itemCart = itemCartRepository.findByProductIdAndCartId(data.getProductId(),id);
            totalQuantity = itemCart.getQuantity() + 1;
            itemCart.setQuantity(totalQuantity);

        }

        Set<ItemCart> itemCarts = cart.getItemCart();
        itemCarts.add(itemCart);
        cart.setItemCart(itemCarts);
        cartRepository.save(cart);
        var cartItemCartDto = cartMapper.toDto(itemCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
    }

}