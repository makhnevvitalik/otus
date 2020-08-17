package com.otus.order.service.delivery;

import lombok.Data;

@Data
public class DeliveryStatusEvent {

    private String orderId;
    private DeliveryStatus status;

    public enum DeliveryStatus {
        CONFIRMED,
        SENT,
        DELIVERED,
        REJECTED
    }
}
