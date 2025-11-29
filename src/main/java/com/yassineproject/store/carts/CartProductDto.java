package com.yassineproject.store.carts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartProductDto {
    private Long productId;
    private String  name;
    private String description;
}
