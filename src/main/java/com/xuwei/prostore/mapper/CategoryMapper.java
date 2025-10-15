package com.xuwei.prostore.mapper;

import com.xuwei.prostore.dto.CategoryDto;
import com.xuwei.prostore.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto categoryDto);
    List<CategoryDto> toDto(List<Category> categories);
}