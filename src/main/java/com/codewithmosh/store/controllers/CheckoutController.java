package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CheckOutRequestDto;
import com.codewithmosh.store.dtos.CheckOutResponseDto;
import com.codewithmosh.store.exception.CartEmptyException;
import com.codewithmosh.store.exception.CartNotFoundException;
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
    public CheckOutResponseDto checkOut(
            @Valid @RequestBody CheckOutRequestDto request
            )
    {

        return orderService.CheckingOut(request);

    }
    @ExceptionHandler({CartNotFoundException.class , CartEmptyException.class})
    public ResponseEntity<Map<String,String>> handleException(Exception ex){
        return ResponseEntity.status(400).body(
                Map.of("error" , ex.getMessage())
        );
    }

}
