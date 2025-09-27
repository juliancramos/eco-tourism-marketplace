package com.marketplace.cartservice.mapper;

import java.util.List;

import com.marketplace.cartservice.dto.OrderDTO;
import com.marketplace.cartservice.dto.OrderItemDTO;
import com.marketplace.cartservice.model.Order;
import com.marketplace.cartservice.model.OrderItem;

public class OrderMapper {

    public static OrderDTO toDto(Order order) {
        if (order == null) return null;
        List<OrderItemDTO> items = order.getItems() == null 
            ? List.of()
            : order.getItems().stream()
                    .map(OrderMapper::toItemDTO)
                    .toList();

        return new OrderDTO(
            order.getId(),
            order.getCartId(),
            order.getUserId(),
            order.getTotal(),
            order.getStatus(),
            order.getOrderDate(),
            items
        );
    }

    private static OrderItemDTO toItemDTO(OrderItem item) {
        return new OrderItemDTO(item.getId(), item.getServiceId(), item.getQuantity(), item.getPrice());
    }
    public static Order toEntity(OrderDTO dto) {
        if (dto == null) return null;
        Order order = new Order();
        order.setId(dto.id());
        order.setUserId(dto.userId());
        order.setTotal(dto.total());
        order.setStatus(dto.status());
        order.setOrderDate(dto.orderDate());

        if (dto.items() != null) {
            order.setItems(dto.items().stream()
                .map(i -> toItemEntity(i, order))
                .toList());
        }

        return order;
    }

        private static OrderItem toItemEntity(OrderItemDTO dto, Order order) {
        OrderItem item = new OrderItem();
        item.setId(dto.id());
        item.setServiceId(dto.serviceId());
        item.setQuantity(dto.quantity());
        item.setPrice(dto.price());
        item.setOrder(order);
        return item;
    }
}
