package com.codewithmosh.store.dtos;

import lombok.*;

@AllArgsConstructor
@Getter
public class ItemCartDto {
    private int quantity;
    private ProductDto product;
}
