package com.otus.order.service.payment;

import lombok.Data;

@Data
public class PaymentEvent {

    private final String orderId;
    private final PaymentStatus status;

    public enum PaymentStatus {
        OK,
        CANCEL
    }
}
