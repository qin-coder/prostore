package com.xuwei.prostore.service.cartItem;

import com.xuwei.prostore.dto.CartDto;
import com.xuwei.prostore.dto.CartItemDto;

public interface CartItemService {

    CartDto addItemToCart(Long cartId, Long productId, int quantity);
    CartDto removeItemFromCart(Long cartId, Long itemId);
    CartDto updateItemQuantity(Long cartId, Long itemId, int quantity);
    CartItemDto getCartItem(Long itemId);
}