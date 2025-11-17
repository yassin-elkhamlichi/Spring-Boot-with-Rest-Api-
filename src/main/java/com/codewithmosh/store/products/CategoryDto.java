package com.codewithmosh.store.products;

import com.codewithmosh.store.carts.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CategoryDto {
    private Long id;
    private List<ProductDto> product;
}
