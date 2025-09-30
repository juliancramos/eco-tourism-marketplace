package com.marketplace.cartservice.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.marketplace.cartservice.dto.AddCartItemRequestDTO;
import com.marketplace.cartservice.dto.CartDTO;
import com.marketplace.cartservice.exception.ResourceNotFoundException;
import com.marketplace.cartservice.mapper.CartMapper;
import com.marketplace.cartservice.model.Cart;
import com.marketplace.cartservice.model.CartItem;
import com.marketplace.cartservice.repository.CartItemRepository;
import com.marketplace.cartservice.repository.CartRepository;
import com.marketplace.cartservice.service.CartService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public void addCartItem(Long cartId, AddCartItemRequestDTO request) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));
                

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setServiceId(request.serviceId());
        item.setQuantity(request.quantity() == null ? 1 : request.quantity());
        item.setDateAdded(LocalDateTime.now());

        // add to cart and persist
        cart.getItems().add(item);
        cartItemRepository.save(item);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartId, Long cartItemId) {
        // Optional: verify that the item belongs to cartId
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found: " + cartItemId));
        if (!item.getCart().getId().equals(cartId)) {
            throw new IllegalArgumentException("CartItem " + cartItemId + " does not belong to cart " + cartId);
        }
        cartItemRepository.delete(item);
    }

    @Transactional
    public CartDTO addItemToCartAndReturn(Long cartId, AddCartItemRequestDTO request) {
        addCartItem(cartId, request);
        return getCart(cartId);
    }

    @Override
    @Transactional
    public CartDTO createCart(Long userId) {
        return cartRepository.findByUserId(userId)
        .map(CartMapper::toDto)
        .orElseGet(() -> {
            Cart c = new Cart();
            c.setUserId(userId);
            c.setCreationDate(LocalDateTime.now());
            return CartMapper.toDto(cartRepository.save(c));
        });
    }

    @Override
    @Transactional
    public CartDTO getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));
        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));
                
        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public Cart getOrCreateCartEntity(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart c = new Cart();
            c.setUserId(userId);
            c.setCreationDate(LocalDateTime.now());
            return cartRepository.save(c);
        });
    }
}
