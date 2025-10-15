package com.xuwei.prostore.service.cart;

import com.xuwei.prostore.dto.CartDto;
import com.xuwei.prostore.model.Cart;

import java.math.BigDecimal;

public interface CartService {

    CartDto getCart(Long id);
    BigDecimal getTotalPrice(Long id);
    CartDto initializeNewCart(Long userId);
    CartDto clearCart(Long id);
    Cart getCartEntityByUserId(Long userId);
    CartDto getCartByUserId(Long userId);

}