package com.codewithmosh.store.dtos;

import com.codewithmosh.store.mappers.ProductMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CategoryDto {
    private Long id;
    private List<ProductDto> product;
}
