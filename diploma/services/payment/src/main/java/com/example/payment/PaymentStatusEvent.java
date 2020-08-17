package com.example.payment;

import lombok.Data;

@Data
public class PaymentStatusEvent {

    private final String orderId;
    private final PaymentStatus status;

    public enum PaymentStatus {
        OK,
        CANCELED
    }
}
