package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.*;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.entities.Status;
import com.codewithmosh.store.exception.ItemNotFoundException;
import com.codewithmosh.store.exception.OrderNotFoundException;
import com.codewithmosh.store.exception.ProductNotFoundException;
import com.codewithmosh.store.exception.UserNotFoundException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.OrdersRepository;
import com.codewithmosh.store.services.AuthService;
import com.codewithmosh.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("orders")
@AllArgsConstructor
public class OrdersController {
    private final OrdersRepository ordersRepository;
    private final AuthService authService;
    private OrderMapper orderMapper;
    private OrderService orderService;


    @GetMapping
    public List<OrderDto> getAllOrders(
    ){
        var user = authService.getCurrentUser();
        List<Orders> orders = ordersRepository.getAllByUser(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }
    @PostMapping
    public ResponseEntity<OrderDto> CreateOrder(
    ){
        Orders order = new Orders();
        order.setStatus(Status.PENDING);
        order.setTotalAmount(0.0);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(authService.getCurrentUser());
        ordersRepository.save(order);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PostMapping("{idOrder}/item")
    public ResponseEntity<ItemCartDto> addItemToOrder(
            @PathVariable Long idOrder,
            @RequestBody AddItemToOrderDto data
    ){
        var orderItemDto = orderService.addItemInOrder(data,idOrder);
        return ResponseEntity.ok(orderItemDto);
    }

    @PutMapping("{idOrder}/item/{idProduct}")
    public ResponseEntity<ItemCartDto> updateItemInOrder(
            @PathVariable Long idOrder,
            @PathVariable Long idProduct,
            @RequestBody UpdateItemInOrder data
    ){
        var orderItemDto = orderService.updateItemInOrder(data,idOrder,idProduct);
        return ResponseEntity.ok(orderItemDto);
    }

    @DeleteMapping("{idOrder}/item/{idProduct}")
    public ResponseEntity<Void> deleteItemFromOrder(
            @PathVariable Long idOrder,
            @PathVariable Long idProduct
    ){
        orderService.deleteItemFromOrder(idOrder,idProduct);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{idOrder}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long idOrder
    ){
        orderService.deleteOrder(idOrder);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("{idOrder}/status")
    public ResponseEntity<OrderDto> changeStatusForOrder(
            @PathVariable Long idOrder,
            @RequestBody OrderStatusUpdateDto statusUpdate
    ){
        OrderDto orderdto = orderService.changeStatus(idOrder,statusUpdate.getStatus().toString());
        return ResponseEntity.ok(orderdto);
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
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleUserException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error" , "User not found")
                );
    }
}
