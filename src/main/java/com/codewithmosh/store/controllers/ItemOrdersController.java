package com.codewithmosh.store.controllers;

import com.codewithmosh.store.entities.Order_items;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.repositories.ItemOrderRepository;
import com.codewithmosh.store.repositories.OrdersRepositroy;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/orderssss")
@AllArgsConstructor
public class ItemOrdersController {
    private final OrdersRepositroy ordersRepositroy;
    private final ItemOrderRepository itemOrderRepository;
    @GetMapping
    public ResponseEntity<?> getAllOrders(){
        var orders = ordersRepositroy.findAll().toArray();
        if(orders == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orders);
    }
}
