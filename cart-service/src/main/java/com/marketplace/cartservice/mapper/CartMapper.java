package com.marketplace.cartservice.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.marketplace.cartservice.dto.CartDTO;
import com.marketplace.cartservice.dto.CartItemDTO;
import com.marketplace.cartservice.model.Cart;
import com.marketplace.cartservice.model.CartItem;

public class CartMapper {
    public static CartDTO toDto(Cart cart) {
        if (cart == null) return null;

        List<CartItemDTO> items = cart.getItems() == null 
            ? List.of() 
            : cart.getItems().stream()
                  .map(CartMapper::toItemDto)
                  .collect(Collectors.toList());

        return new CartDTO(
            cart.getId(),
            cart.getUserId(),
            cart.getCreationDate(),
            items
        );
    }

    public static CartItemDTO toItemDto(CartItem item) {
        if (item == null) return null;
        return new CartItemDTO(
            item.getId(),
            item.getServiceId(),
            item.getQuantity(),
            item.getDateAdded()
        );
    }
}
