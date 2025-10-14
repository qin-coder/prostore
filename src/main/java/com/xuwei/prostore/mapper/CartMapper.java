package com.xuwei.prostore.mapper;

import com.xuwei.prostore.dto.CartDto;
import com.xuwei.prostore.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class, ProductMapper.class})
public interface CartMapper {
    
    @Mapping(target = "cartId", source = "id")
    CartDto toDto(Cart cart);
    
    Cart toEntity(CartDto cartDto);
}