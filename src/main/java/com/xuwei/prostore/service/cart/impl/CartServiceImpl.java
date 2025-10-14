package com.xuwei.prostore.service.cart.impl;

import com.xuwei.prostore.dto.CartDto;
import com.xuwei.prostore.exception.ResourceNotFoundException;
import com.xuwei.prostore.mapper.CartMapper;
import com.xuwei.prostore.model.Cart;
import com.xuwei.prostore.repository.CartItemRepository;
import com.xuwei.prostore.repository.CartRepository;
import com.xuwei.prostore.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Override
    public CartDto getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));
        return cartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public CartDto clearCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));

        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        return getCart(id).getTotalAmount();
    }

    @Override
    public CartDto initializeNewCart() {
        Cart newCart = new Cart();
        Cart savedCart = cartRepository.save(newCart);
        return cartMapper.toDto(savedCart);
    }
}