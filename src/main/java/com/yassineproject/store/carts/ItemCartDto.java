package com.yassineproject.store.carts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemCartDto {
    private int quantity;
    private CartProductDto product;
    private BigDecimal totalPrice;
}