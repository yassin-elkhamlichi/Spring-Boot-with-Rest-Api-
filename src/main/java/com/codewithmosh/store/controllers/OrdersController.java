package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToOrderDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.dtos.UpdateItemInOrder;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.entities.Status;
import com.codewithmosh.store.exception.ItemNotFoundException;
import com.codewithmosh.store.exception.OrderNotFoundException;
import com.codewithmosh.store.exception.ProductNotFoundException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.OrdersRepositroy;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("{idUser}/orders")
@AllArgsConstructor
public class OrdersController {
    private final OrdersRepositroy ordersRepositroy;
    private final UserRepository userRepository;
    private OrderMapper orderMapper;
    private OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders(
            @PathVariable Long idUser
    ){
        return ordersRepositroy.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }
    @PostMapping
    public ResponseEntity<OrderDto> CreateOrder(
            @PathVariable Long idUser
    ){
        Orders order = new Orders();
        order.setStatus(Status.PENDING);
        order.setTotalAmount(0.0);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(userRepository.findById(idUser).orElse(null));
        ordersRepositroy.save(order);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PostMapping("{idOrder}/item")
    public ResponseEntity<ItemCartDto> addItemToOrder(
            @PathVariable Long idUser,
            @PathVariable Long idOrder,
            @RequestBody AddItemToOrderDto data
    ){
        var orderItemDto = orderService.addItemInOrder(data,idOrder);
        return ResponseEntity.ok(orderItemDto);
    }

    @PostMapping("{idOrder}/item/{idItem}")
    public ResponseEntity<ItemCartDto> updateItemInOrder(
            @PathVariable Long idUser,
            @PathVariable Long idOrder,
            @PathVariable Long idItem,
            @RequestBody UpdateItemInOrder data
    ){
        var orderItemDto = orderService.updateItemInOrder(data,idOrder,idItem);
        return ResponseEntity.ok(orderItemDto);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleOrderException(){
        return ResponseEntity.status(404).body(
                Map.of("error" , "Order not found")
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error" , "item not found")
        );
    }
}
