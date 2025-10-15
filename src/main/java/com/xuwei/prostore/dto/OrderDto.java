package com.xuwei.prostore.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
@Data
public class OrderDto {
    private Long orderId;
    private LocalDate orderDate;
    private Set<OrderItemDto> orderItems;
    private BigDecimal totalAmount;
}
