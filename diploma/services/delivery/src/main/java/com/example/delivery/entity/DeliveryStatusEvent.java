package com.example.delivery.entity;

import java.time.Instant;
import lombok.Data;

@Data
public class DeliveryStatusEvent {

    private String orderId;
    private String clientId;
    private DeliveryStatus status;
    private Instant createdAt;
}
