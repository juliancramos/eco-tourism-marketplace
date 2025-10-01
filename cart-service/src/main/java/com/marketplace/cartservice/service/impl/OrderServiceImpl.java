package com.marketplace.cartservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.marketplace.cartservice.dto.OrderDTO;
import com.marketplace.cartservice.exception.ResourceNotFoundException;
import com.marketplace.cartservice.mapper.OrderMapper;
import com.marketplace.cartservice.model.Cart;
import com.marketplace.cartservice.model.CartItem;
import com.marketplace.cartservice.model.Order;
import com.marketplace.cartservice.model.OrderItem;
import com.marketplace.cartservice.repository.CartRepository;
import com.marketplace.cartservice.repository.OrderItemRepository;
import com.marketplace.cartservice.repository.OrderRepository;
import com.marketplace.cartservice.service.OrderService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public String updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);
        return order.getStatus();
    }

    @Override
    @Transactional
    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        return OrderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDTO checkout(Long userId, String paymentMethod) {
        // Get or create cart for user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty for user: " + userId);
        }

        Double total = cart.getItems().stream()
                .mapToDouble(this::priceForItem)
                .sum();

        // create order
        Order order = new Order();
        order.setUserId(userId);
        order.setCartId(cart.getId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotal(total);
        order.setPaymentMethod(paymentMethod);
        
        Order savedOrder = orderRepository.save(order);

        // create order items
        List<OrderItem> orderItems = cart.getItems().stream().map(ci -> {
            OrderItem oi = new OrderItem();
            oi.setOrder(savedOrder);
            oi.setServiceId(ci.getServiceId());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(priceForItem(ci));
            return oi;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        // set items to order and save
        order.setItems(orderItems);
        order = orderRepository.save(order);

        // clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        // build DTO
        OrderDTO dto = OrderMapper.toDto(order);

        return dto;
    }

    @Transactional
    public List<OrderDTO> findOrdersByUser(Long userId) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getUserId().equals(userId))
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    private double priceForItem(CartItem ci) {
        return (double) ci.getQuantity() * 1.0d;
    }

}
