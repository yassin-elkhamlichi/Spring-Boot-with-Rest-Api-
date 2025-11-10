package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.AddItemToOrderDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.dtos.UpdateItemInOrder;
import com.codewithmosh.store.entities.Order_items;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.exception.ItemNotFoundException;
import com.codewithmosh.store.exception.OrderNotFoundException;
import com.codewithmosh.store.exception.ProductNotFoundException;
import com.codewithmosh.store.mappers.Order_itemsMapper;
import com.codewithmosh.store.repositories.ItemOrderRepository;
import com.codewithmosh.store.repositories.OrdersRepositroy;
import com.codewithmosh.store.repositories.ProductRepository;
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

    public ItemCartDto updateItemInOrder(UpdateItemInOrder data, Long idOrder, Long idItem) {
        Order_items item = itemOrderRepository.findById(idItem).orElse(null);
        if (item == null) {
            throw new ItemNotFoundException();
        }
        Orders order = ordersRepositroy.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
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
}
