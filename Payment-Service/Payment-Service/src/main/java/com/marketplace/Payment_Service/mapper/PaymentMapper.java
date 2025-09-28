package com.marketplace.Payment_Service.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.marketplace.Payment_Service.dto.OrderDTOs.OrderDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.CreatePaymentDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.PaymentDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.UpdatePaymentDTO;
import com.marketplace.Payment_Service.dto.PaymentDetailsDTOs.CreatePaymentDetailsDTO;
import com.marketplace.Payment_Service.dto.PaymentDetailsDTOs.PaymentDetailDTO;
import com.marketplace.Payment_Service.dto.ReceiptDTOs.ReceiptDTO;
import com.marketplace.Payment_Service.model.Payment;
import com.marketplace.Payment_Service.model.PaymentDetail;
import com.marketplace.Payment_Service.model.PaymentMethod;
import com.marketplace.Payment_Service.model.PaymentStatus;
import com.marketplace.Payment_Service.model.Receipt;

public class PaymentMapper {
    public static PaymentDTO toDto(Payment e) {
        List<PaymentDetailDTO> details;
            details = e.getPaymentDetails() == null ? List.of() :
                    e.getPaymentDetails().stream()
                            .map(d -> new PaymentDetailDTO(d.getId(), d.getServiceId(), d.getServiceName() , d.getQuantity(), d.getUnitPrice(), d.getSubtotal()))
                            .toList();
        ReceiptDTO receipt;                    
            receipt = e.getReceipt() == null ? null :
                    new ReceiptDTO(e.getReceipt().getId(), e.getId(), e.getReceipt().getReceiptNumber(), e.getReceipt().getGeneratedAt(), e.getReceipt().getTotals(), e.getReceipt().getItems());              
        return new PaymentDTO(
            e.getId(),
            e.getOrderId(),
            e.getUserId(),
            e.getAmount(),
            e.getCurrency(),
            e.getStatus().name(),
            e.getMethod().name(),
            e.getTransactionId(),
            e.getCallbackTopic(),
            e.getCreationDate(),
            e.getTimestamp(),
            details,
            receipt
        );
    }

    public static void applyCreate(Payment e, CreatePaymentDTO dto, OrderDTO order, LocalDateTime now) {
        e.setOrderId(order.orderId());
        e.setUserId(order.userId());
        e.setAmount(order.total());
        e.setCurrency(dto.currency());
        e.setMethod(PaymentMethod.valueOf(dto.paymentMethod()));
        e.setStatus(PaymentStatus.valueOf(order.status()));
        e.setTransactionId(UUID.randomUUID().toString());
        e.setCallbackTopic("payments.response");
        e.setCreationDate(order.orderDate());
        e.setTimestamp(now);
        if(dto.details() != null) {
            for(CreatePaymentDetailsDTO d : dto.details()) {
                PaymentDetail pd = new PaymentDetail();
                pd.setPayment(e);
                pd.setServiceId(d.serviceId());
                pd.setServiceName(d.ServiceName());
                pd.setQuantity(d.quantity());
                pd.setUnitPrice(d.unitPrice());
                pd.setSubtotal(d.subtotal());
            }
        } 
        e.setPaymentDetails(e.getPaymentDetails());

        Receipt receipt = new Receipt();
        receipt.setPayment(e);
        receipt.setPaymentId(e.getId());
        receipt.setReceiptNumber(dto.receipt().receiptNumber());
        receipt.setGeneratedAt(now);
        receipt.setTotals(order.total());
        receipt.setItems(dto.receipt().items());
        e.setReceipt(receipt);
    }

    public static void applyUpdateStatus(Payment e, UpdatePaymentDTO dto, LocalDateTime now) {
        e.setStatus(PaymentStatus.valueOf(dto.status()));
        e.setCallbackTopic(dto.callbackTopic());
        e.setTimestamp(now);
    }

    public static void applyUpdate(Payment payment, UpdatePaymentDTO dto) {
        payment.setStatus(PaymentStatus.valueOf(dto.status()));
        payment.setCallbackTopic(dto.callbackTopic());
        payment.setTimestamp(LocalDateTime.now());
    }
}
