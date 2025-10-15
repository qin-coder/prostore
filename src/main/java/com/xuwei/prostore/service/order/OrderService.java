package com.xuwei.prostore.service.order;

import com.xuwei.prostore.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrderById(Long orderId);
}
