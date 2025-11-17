package com.codewithmosh.store.order;

import com.codewithmosh.store.carts.CartProductDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private int quantity;
    private CartProductDto product;
    private BigDecimal totalPrice;
}
