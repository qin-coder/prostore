package com.xuwei.prostore.service.order.impl;

import com.xuwei.prostore.dto.CartDto;
import com.xuwei.prostore.dto.OrderDto;
import com.xuwei.prostore.exception.ResourceNotFoundException;
import com.xuwei.prostore.mapper.OrderMapper;
import com.xuwei.prostore.model.Order;
import com.xuwei.prostore.model.OrderItem;
import com.xuwei.prostore.model.Product;
import com.xuwei.prostore.repository.OrderRepository;
import com.xuwei.prostore.repository.ProductRepository;
import com.xuwei.prostore.service.cart.CartService;
import com.xuwei.prostore.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final ProductRepository productRepository;


    @Override
    public OrderDto placeOrder(Long userId) {
        CartDto cart = cartService.getCart(userId);
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalPrice(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getCartId());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDto(order);

    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> createOrderItems(Order order,
                                             CartDto cart) {
        return cart.getItems().stream().map(cartItemDto -> {
            Long productId = cartItemDto.getProduct().getId();
            Product product =
                    productRepository.findById(productId)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

            product.setInventory(product.getInventory() - cartItemDto.getQuantity());
            productRepository.save(product);

            return new OrderItem(order, product,
                    cartItemDto.getQuantity(),
                    cartItemDto.getUnitPrice());
        }).toList();
    }

}

