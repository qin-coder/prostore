package com.xuwei.prostore.service.cart.impl;

import com.xuwei.prostore.dto.CartDto;
import com.xuwei.prostore.exception.ResourceNotFoundException;
import com.xuwei.prostore.mapper.CartMapper;
import com.xuwei.prostore.model.Cart;
import com.xuwei.prostore.model.CartItem;
import com.xuwei.prostore.model.User;
import com.xuwei.prostore.repository.CartItemRepository;
import com.xuwei.prostore.repository.CartRepository;
import com.xuwei.prostore.repository.UserRepository;
import com.xuwei.prostore.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;

    @Override
    public CartDto getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart not found with id: " + id));
        return cartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public CartDto clearCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));

        Set<CartItem> itemsToRemove = new HashSet<>(cart.getItems());
        for (CartItem item : itemsToRemove) {
            cart.removeItem(item);
        }

        cart.setTotalAmount(BigDecimal.ZERO);
        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        return getCart(id).getTotalAmount();
    }

    @Override
    public CartDto initializeNewCart(Long userId) {
        Optional<Cart> existingCart = cartRepository.findByUser_UserId(userId);
        if (existingCart.isPresent()) {
            return cartMapper.toDto(existingCart.get());
        }

        Cart newCart = new Cart();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        newCart.setUser(user);
        Cart savedCart = cartRepository.save(newCart);
        return cartMapper.toDto(savedCart);
    }

    @Override
    public Cart getCartEntityByUserId(Long userId) {
        return cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart not found for userId " + userId));
    }

    @Override
    public CartDto getCartByUserId(Long userId) {
        return cartRepository.findByUser_UserId(userId)
                .map(cartMapper::toDto)
                .orElse(null);
    }

}