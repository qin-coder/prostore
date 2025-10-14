package com.xuwei.prostore.service.cartItem;

import com.xuwei.prostore.dto.CartItemDto;

public interface CartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long itemId);
    void updateItemQuantity(Long cartId, Long itemId, int quantity);
    CartItemDto getCartItem(Long itemId);
}