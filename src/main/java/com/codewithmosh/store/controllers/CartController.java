package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Data
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            @RequestBody CartDto cartDto
    ){
        var cart = cartMapper.toCart(cartDto);
        cart.setDateCreated(new Date());
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.toCartDto(cart));
    }
}
