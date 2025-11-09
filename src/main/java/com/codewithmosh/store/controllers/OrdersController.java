package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToOrderDto;
import com.codewithmosh.store.dtos.CartProductDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.entities.Status;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.mappers.Order_itemsMapper;
import com.codewithmosh.store.repositories.ItemOrderRepository;
import com.codewithmosh.store.repositories.OrdersRepositroy;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("{idUser}/orders")
@AllArgsConstructor
public class OrdersController {
    private final OrdersRepositroy ordersRepositroy;
    private final ItemOrderRepository itemOrderRepository;
    private final UserRepository userRepository;
    private OrderMapper orderMapper;
    private Order_itemsMapper order_itemsMapper;
    private ProductRepository productRepository;

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
    public ResponseEntity<ItemCartDto> addItemtoOrder(
            @PathVariable Long idUser,
            @PathVariable Long idOrder,
            @RequestBody AddItemToOrderDto data
    ){
        var order = ordersRepositroy.findById(idOrder).orElse(null);
        var item = order_itemsMapper.toEntity(data);
        Product product = productRepository.findById(data.getProductId()).orElse(null);
        if(order == null){
            return ResponseEntity.notFound().build();
        }
        if (product == null){
            return ResponseEntity.notFound().build();
        }
        if(!itemOrderRepository.existsByProductId(data.getProductId())){
            item.setOrder(order);
            order.getOrder_items().add(item);
            item.setQuantity(1);
            item.setProduct(product);
        }
        else {
            int quantity = item.getQuantity() + 1;
            order.getOrder_items().forEach(
                    item1 -> {
                        if (item.getProduct() == item1.getProduct())
                            item.setQuantity(quantity);
                    }
            );
        }
        itemOrderRepository.save(item);
        return ResponseEntity.ok(order_itemsMapper.toDto(item));
    }
}
