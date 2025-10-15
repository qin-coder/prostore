package com.xuwei.prostore.mapper;

import com.xuwei.prostore.dto.OrderDto;
import com.xuwei.prostore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, ProductMapper.class})
public interface OrderMapper {
    @Mapping(target = "orderId", source = "orderId")
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);
}
