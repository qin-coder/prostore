package com.xuwei.prostore.service.category;

import com.xuwei.prostore.dto.CategoryDto;
import com.xuwei.prostore.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<CategoryDto> getAllCategories();
    CategoryDto addCategory(Category category);
    CategoryDto updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);
}