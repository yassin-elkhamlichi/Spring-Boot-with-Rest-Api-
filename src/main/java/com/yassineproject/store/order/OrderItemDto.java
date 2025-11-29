package com.yassineproject.store.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private int quantity;
    private OrderProductDto product;
    private BigDecimal totalPrice;
}
