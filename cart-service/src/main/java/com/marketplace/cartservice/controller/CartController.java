package com.marketplace.cartservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.cartservice.dto.AddCartItemRequestDTO;
import com.marketplace.cartservice.dto.CartDTO;
import com.marketplace.cartservice.service.CartService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<CartDTO> createCart(@PathVariable Long userId) {
        CartDTO dto = cartService.createCart(userId);
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long cartId) {
        CartDTO dto = cartService.getCart(cartId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}/current")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO dto = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Void> addCartItem(@PathVariable Long cartId,
            @RequestBody AddCartItemRequestDTO request) {
        cartService.addCartItem(cartId, request);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartId,
            @PathVariable Long cartItemId) {
        cartService.removeCartItem(cartId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
