package com.xuwei.prostore.service.cart;

import com.xuwei.prostore.dto.CartDto;

import java.math.BigDecimal;

public interface CartService {

    CartDto getCart(Long id);
    BigDecimal getTotalPrice(Long id);
    CartDto initializeNewCart();
    CartDto clearCart(Long id);
}