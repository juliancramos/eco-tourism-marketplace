package com.marketplace.Payment_Service.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.marketplace.Payment_Service.dto.PaymentDTO;
import com.marketplace.Payment_Service.dto.PaymentDetailDTO;
import com.marketplace.Payment_Service.dto.ReceiptDTO;
import com.marketplace.Payment_Service.dto.OrderDTOs.OrderDTO;
import com.marketplace.Payment_Service.model.Payment;
import com.marketplace.Payment_Service.model.PaymentDetail;
import com.marketplace.Payment_Service.model.PaymentMethod;
import com.marketplace.Payment_Service.model.PaymentStatus;

public class PaymentMapper {
    public static PaymentDTO toDto(Payment e) {
        List<PaymentDetailDTO> items;
            items = e.getItems() == null ? List.of() :
                    e.getItems().stream()
                            .map(d -> new PaymentDetailDTO(d.getId(), d.getServiceId(), d.getQuantity(), d.getPrice()))
                            .toList();       
        ReceiptDTO receipt = e.getReceipt() == null ? null :
            new ReceiptDTO(
                e.getReceipt().getId(),
                e.getReceipt().getReceiptNumber(),
                e.getReceipt().getGeneratedAt(),
                e.getReceipt().getTotal()
            );                                                            
        return new PaymentDTO(
            e.getId(),
            e.getOrderId(),
            e.getCartId(),
            e.getUserId(),
            e.getTotal(),
            e.getStatus().name(),
            e.getMethod().name(),
            e.getTransactionId(),
            e.getOrderDate(),
            items,
            receipt
        );
    }

    public static void applyCreate(Payment e, OrderDTO order, LocalDateTime now) {
        e.setOrderId(order.id());
        e.setCartId(order.cartId());
        e.setUserId(order.userId());
        e.setTotal(order.total());
        e.setMethod(PaymentMethod.valueOf(order.paymentMethod()));
        e.setStatus(PaymentStatus.valueOf(order.status()));
        e.setTransactionId(UUID.randomUUID().toString());
        e.setOrderDate(order.orderDate());
        e.setItems(order.items().stream().map(i -> {
            PaymentDetail pd = new PaymentDetail();
            pd.setPayment(e);
            pd.setServiceId(i.serviceId());
            pd.setQuantity(i.quantity());
            pd.setPrice(i.price());
            return pd;
        }).toList());
        e.setReceipt(null);
    }
}
