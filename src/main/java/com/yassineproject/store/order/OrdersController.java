package com.yassineproject.store.order;

import com.yassineproject.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
