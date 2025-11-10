package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.AddItemToOrderDto;
import com.codewithmosh.store.dtos.ItemCartDto;
import com.codewithmosh.store.entities.Order_items;
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
        Order_items order_items ;

        int quantity = 1;
        if (itemOrderRepository.existsByProductId(data.getProductId())) {
            if(itemOrderRepository.existsByOrderId(idOrder)) {
                order_items = itemOrderRepository.findByProductIdAndOrderId(data.getProductId(), idOrder);
                quantity = order_items.getQuantity() + 1;
                int finalQuantity = quantity;
                order.getOrder_items().forEach(
                        item -> {
                            if (item.getProduct().getId().equals(data.getProductId())) {
                                item.setQuantity(finalQuantity);
                            }
                        }
                );
            }else{
                order_items = Order_items.builder()
                        .product(product)
                        .order(order)
                        .build();
                order.getOrder_items().add(order_items);
            }
        } else {
            order_items = Order_items.builder()
                    .product(product)
                    .order(order)
                    .build();
            order.getOrder_items().add(order_items);
        }
        order_items.setUnit_price(product.getPrice());
        BigDecimal price = BigDecimal.ZERO;
        price = order_items.getUnit_price().multiply(BigDecimal.valueOf(quantity));
        order_items.setQuantity(quantity);
        order_items.setTotal_amount(price);
        BigDecimal finalPrice = price;
        if(!order.getOrder_items().isEmpty()){
            order.getOrder_items().forEach(
                    item->{
                        if(item.getProduct().getId().equals(data.getProductId())){
                            item.setQuantity(order_items.getQuantity());
                            item.setTotal_amount(finalPrice);
                        }
                    });
        }
        ordersRepositroy.save(order);
        var orderItemdDto = order_itemsMapper.toDto(order_items);
        orderItemdDto.setTotalPrice(price);
        orderItemdDto.getProduct().setProductId(data.getProductId());
        return orderItemdDto;
    }
}
