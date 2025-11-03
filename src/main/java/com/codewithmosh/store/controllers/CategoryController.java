package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CategoryDto;
import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Category;
import com.codewithmosh.store.mappers.CategoryMapper;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Getter
@RestController
@RequestMapping("/categories")
public class CategoryController {
    public final CategoryRepository categoryRepository;
    public final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    @GetMapping
    public List<ProductDto> getAllCategories(@RequestParam(required = false ,  defaultValue = "0" ,  name = "category_id") Long category_id){
        if(category_id == 0 ) {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
        }
        return categoryRepository.findById(Byte.parseByte(String.valueOf(category_id)))
            .map(Category::getProducts)
            .orElse(new HashSet<>())
            .stream()
            .map(productMapper::toDto)
            .toList();
    }
}
