package com.xuwei.prostore.mapper;

import com.xuwei.prostore.dto.OrderDto;
import com.xuwei.prostore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "totalAmount", source = "totalPrice")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> orders);

    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "totalPrice", source = "totalAmount")
    Order toEntity(OrderDto orderDto);
}