package com.xuwei.prostore.service.cartItem.impl;

import com.xuwei.prostore.dto.CartItemDto;
import com.xuwei.prostore.exception.ResourceNotFoundException;
import com.xuwei.prostore.mapper.CartItemMapper;
import com.xuwei.prostore.model.Cart;
import com.xuwei.prostore.model.CartItem;
import com.xuwei.prostore.model.Product;
import com.xuwei.prostore.repository.CartItemRepository;
import com.xuwei.prostore.repository.CartRepository;
import com.xuwei.prostore.service.cart.CartService;
import com.xuwei.prostore.service.cartItem.CartItemService;
import com.xuwei.prostore.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartItemMapper cartItemMapper;

    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Product product = productService.getProductById(productId);

        // Check if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem(product, quantity, product.getPrice());
            cartItem.setCart(cart);
            cart.addItem(cartItem);
        }

        cartItem.setTotalPrice();
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem itemToRemove = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Verify the item belongs to the cart
        if (!itemToRemove.getCart().getId().equals(cartId)) {
            throw new ResourceNotFoundException("Cart item not found in specified cart");
        }

        cart.removeItem(itemToRemove);
        cartItemRepository.delete(itemToRemove);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Verify the item belongs to the cart
        if (!cartItem.getCart().getId().equals(cartId)) {
            throw new ResourceNotFoundException("Cart item not found in specified cart");
        }

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice();
        cartItemRepository.save(cartItem);

        // Update cart total amount
        cart.updateTotalAmount();
        cartRepository.save(cart);
    }

    @Override
    public CartItemDto getCartItem(Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        return cartItemMapper.toDto(cartItem);
    }
}