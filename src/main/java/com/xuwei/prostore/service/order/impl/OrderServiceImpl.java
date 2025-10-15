package com.xuwei.prostore.service.order.impl;

import com.xuwei.prostore.dto.OrderDto;
import com.xuwei.prostore.enums.OrderStatus;
import com.xuwei.prostore.exception.ResourceNotFoundException;
import com.xuwei.prostore.mapper.OrderMapper;
import com.xuwei.prostore.model.*;
import com.xuwei.prostore.repository.CartRepository;
import com.xuwei.prostore.repository.OrderRepository;
import com.xuwei.prostore.repository.ProductRepository;
import com.xuwei.prostore.service.cart.CartService;
import com.xuwei.prostore.service.order.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Override
    public OrderDto placeOrder(Long userId) {
        try {
            log.info("Attempting to place order for user ID: {}", userId);

            Cart cart = getCartForOrder(userId);
            log.info("Found cart: {} with {} items", cart.getId(), cart.getItems().size());

            if (cart.getItems().isEmpty()) {
                throw new IllegalArgumentException("Cart is empty. Please add items to cart before placing order.");
            }

            Order order = createOrder(cart);
            List<OrderItem> orderItemList = createOrderItems(order, cart);
            order.setOrderItems(new HashSet<>(orderItemList));
            order.setTotalPrice(calculateTotalAmount(orderItemList));

            log.info("Saving order with {} items", orderItemList.size());
            Order savedOrder = orderRepository.save(order);

            clearCartForOrder(cart.getId());

            log.info("Order created successfully with ID: {}", savedOrder.getOrderId());
            return orderMapper.toDto(savedOrder);
        } catch (Exception e) {
            log.error("Error placing order for user {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    private Cart getCartForOrder(Long userId) {

        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));

        if (cart.getItems() != null) {
            cart.getItems().size();
        }

        return cart;
    }

    private void clearCartForOrder(Long cartId) {
        try {

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));

            Set<CartItem> itemsToRemove = new HashSet<>(cart.getItems());
            for (CartItem item : itemsToRemove) {
                cart.removeItem(item);
            }

            cart.setTotalAmount(BigDecimal.ZERO);
            cartRepository.save(cart);

            log.info("Cart {} cleared successfully", cartId);
        } catch (Exception e) {
            log.error("Error clearing cart {}: {}", cartId, e.getMessage(), e);
        }
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Long productId = cartItem.getProduct().getId();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

            // 详细的库存检查
            if (product.getInventory() < cartItem.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient inventory for product: " + product.getName() +
                                ". Available: " + product.getInventory() +
                                ", Requested: " + cartItem.getQuantity()
                );
            }

            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getUnitPrice()); // 确保这个字段被设置

            log.info("Created order item: productId={}, quantity={}, price={}",
                    productId, cartItem.getQuantity(), cartItem.getUnitPrice());

            return orderItem;
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);
        return orderMapper.toDto(orders);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}