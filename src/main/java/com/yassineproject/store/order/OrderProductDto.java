package com.yassineproject.store.order;

import com.yassineproject.store.carts.CartProductDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private int quantity;
    private CartProductDto product;
    private BigDecimal totalPrice;
}
