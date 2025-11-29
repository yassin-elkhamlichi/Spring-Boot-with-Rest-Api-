package com.yassineproject.store.products;

import com.yassineproject.store.carts.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CategoryDto {
    private Long id;
    private List<ProductDto> product;
}
