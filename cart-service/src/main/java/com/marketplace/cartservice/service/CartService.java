package com.marketplace.cartservice.service;

import com.marketplace.cartservice.dto.AddCartItemRequestDTO;
import com.marketplace.cartservice.dto.CartDTO;

public interface CartService {

    void addCartItem(Long cartId, AddCartItemRequestDTO request);
    void removeCartItem(Long cartId, Long cartItemId);

    CartDTO createCart(Long userId);
    CartDTO getCart(Long cartId);
    CartDTO getCartByUserId(Long userId);

    void clearCart(Long cartId);
}
