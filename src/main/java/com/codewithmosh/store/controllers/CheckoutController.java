package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CheckOutRequestDto;
import com.codewithmosh.store.dtos.CheckOutResponseDto;
import com.codewithmosh.store.exception.CartEmptyException;
import com.codewithmosh.store.exception.CartNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.services.CartService;
import com.codewithmosh.store.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<CheckOutResponseDto> checkOut(
            @Valid @RequestBody CheckOutRequestDto request
            )
    {
        CheckOutResponseDto response = orderService.CheckingOut(request);
        return ResponseEntity.ok(response);

    }
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFoundException(){
        return ResponseEntity.status(400).body(
                Map.of("error" , "Cart not found")
        );
    }
    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<Map<String,String>> handleCartEmptyException(){
        return ResponseEntity.status(400).body(
                Map.of("error" , "Cart Empty")
        );
    }
}
