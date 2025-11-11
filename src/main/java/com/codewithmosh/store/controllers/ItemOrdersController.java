package com.codewithmosh.store.controllers;

import com.codewithmosh.store.repositories.ItemOrderRepository;
import com.codewithmosh.store.repositories.OrdersRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/orderssss")
@AllArgsConstructor
public class ItemOrdersController {
    private final OrdersRepository ordersRepository;
    private final ItemOrderRepository itemOrderRepository;
    @GetMapping
    public ResponseEntity<?> getAllOrders(){
        var orders = ordersRepository.findAll().toArray();
        if(orders == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orders);
    }
}
