package com.yassineproject.store.products;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CategoryMapper {
    public CategoryDto  toDto(Category category);
}
