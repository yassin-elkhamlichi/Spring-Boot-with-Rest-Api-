package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CategoryDto;
import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Category;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-03T21:48:18+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDto toDto(Category category) {
        if ( category == null ) {
            return null;
        }

        Long id = null;

        if ( category.getId() != null ) {
            id = category.getId().longValue();
        }

        List<ProductDto> product = null;

        CategoryDto categoryDto = new CategoryDto( id, product );

        return categoryDto;
    }
}
