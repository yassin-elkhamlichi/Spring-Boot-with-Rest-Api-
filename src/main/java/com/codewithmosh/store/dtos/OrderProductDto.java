package com.codewithmosh.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private int quantity;
    private CartProductDto product;
    private BigDecimal totalPrice;
}
