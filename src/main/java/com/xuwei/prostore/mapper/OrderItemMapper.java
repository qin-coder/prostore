package com.xuwei.prostore.mapper;

import com.xuwei.prostore.dto.OrderItemDto;
import com.xuwei.prostore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {

    @Mapping(target = "itemId", source = "orderItemId")
    @Mapping(target = "unitPrice", source = "price")
    @Mapping(target = "totalPrice", source = ".", qualifiedByName = "calculateTotalPrice")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "orderItemId", source = "itemId")
    @Mapping(target = "price", source = "unitPrice")
    OrderItem toEntity(OrderItemDto orderItemDto);

    @Named("calculateTotalPrice")
    default BigDecimal calculateTotalPrice(OrderItem orderItem) {
        if (orderItem.getPrice() != null && orderItem.getQuantity() > 0) {
            return orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        }
        return BigDecimal.ZERO;
    }
}