package com.codewithmosh.store.order;

import com.codewithmosh.store.carts.ItemCartDto;
import com.codewithmosh.store.carts.ItemNotFoundException;
import com.codewithmosh.store.products.ProductNotFoundException;
import com.codewithmosh.store.users.UserNotFoundException;
import com.codewithmosh.store.auth.AuthService;
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


    @GetMapping
    public List<OrderDto> getAllOrders(
    ){
        var user = authService.getCurrentUser();
        List<Orders> orders = ordersRepository.getAllByUser(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

}
