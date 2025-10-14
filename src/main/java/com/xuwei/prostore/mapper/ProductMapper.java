package com.xuwei.prostore.mapper;

import com.xuwei.prostore.dto.CategoryDto;
import com.xuwei.prostore.dto.ImageDto;
import com.xuwei.prostore.dto.ProductDto;
import com.xuwei.prostore.model.Category;
import com.xuwei.prostore.model.Image;
import com.xuwei.prostore.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    CategoryDto toDto(Category category);
    ImageDto toDto(Image image);
    List<ImageDto> toImageDtoList(List<Image> images);
}