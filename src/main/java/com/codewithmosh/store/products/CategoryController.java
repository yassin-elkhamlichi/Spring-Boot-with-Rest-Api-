package com.codewithmosh.store.products;

import com.codewithmosh.store.carts.ProductDto;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
