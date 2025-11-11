package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.AddItemToOrderDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.dtos.UpdateItemInOrder;
import com.codewithmosh.store.entities.Order_items;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.entities.Status;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exception.OrderNotFoundException;
import com.codewithmosh.store.exception.ProductNotFoundException;
import com.codewithmosh.store.exception.UserNotFoundException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.mappers.Order_itemsMapper;
import com.codewithmosh.store.repositories.ItemOrderRepository;
import com.codewithmosh.store.repositories.OrdersRepositroy;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final ItemOrderRepository itemOrderRepository;
    private final OrdersRepositroy ordersRepositroy;
    private final Order_itemsMapper order_itemsMapper;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;


    public ItemCartDto addItemInOrder(AddItemToOrderDto data,Long idOrder){
        var product = productRepository.findById(data.getProductId()).orElse(null);
        if (product == null) {
           throw new ProductNotFoundException();
        }
        var order = ordersRepositroy.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        Order_items order_items = order.addToOrder(product) ;

        var orderItemDto = order_itemsMapper.toDto(order_items);
        orderItemDto.setTotalPrice(order_items.getTotal_amount());
        orderItemDto.getProduct().setProductId(order_items.getProduct().getId());
        ordersRepositroy.save(order);

        return orderItemDto;
    }

    public ItemCartDto updateItemInOrder(UpdateItemInOrder data, Long idOrder, Long idProduct) {
        var product = productRepository.findById(idProduct).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        Orders order = ordersRepositroy.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        Order_items item = itemOrderRepository.findByProductIdAndOrderId(idProduct,idOrder);
        item.setQuantity(data.getQuantity());
        item.setTotal_amount(item.getUnit_price().multiply(BigDecimal.valueOf(data.getQuantity())));

        order.updateQuantityinItem(data.getQuantity(),item);
        order.updateTotalAmount(data.getQuantity(),item);

        ordersRepositroy.save(order);

        var orderItemdDto = order_itemsMapper.toDto(item);
        orderItemdDto.setTotalPrice(item.getTotal_amount());
        orderItemdDto.getProduct().setProductId(item.getProduct().getId());
        return orderItemdDto;
    }

    public void deleteItemFromOrder(Long idOrder, Long idProduct) {
        var product = productRepository.findById(idProduct).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        Orders order = ordersRepositroy.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        order.removeItemOrder(idProduct);
        ordersRepositroy.save(order);
    }

    public void deleteOrder(Long idOrder) {
        Orders order = ordersRepositroy.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        ordersRepositroy.delete(order);
    }

    public OrderDto changeStatus(Long idOrder, String status , Long idUser) {
        Orders order = ordersRepositroy.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        User user = userRepository.findById(idUser).orElse(null);
        if(user == null) {
            throw new UserNotFoundException();
        }
        order.changeStatus(status);
        ordersRepositroy.save(order);
        return orderMapper.toDto(order);
    }
}
