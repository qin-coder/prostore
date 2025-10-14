package com.xuwei.prostore.service.cart;

import com.xuwei.prostore.dto.CartDto;

import java.math.BigDecimal;

public interface CartService {
    CartDto getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Long initializeNewCart();
}