package com.xuwei.prostore.mapper;

import com.xuwei.prostore.dto.CartItemDto;
import com.xuwei.prostore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartItemMapper {
    
    @Mapping(target = "itemId", source = "id")
    CartItemDto toDto(CartItem cartItem);
    
    CartItem toEntity(CartItemDto cartItemDto);
}