package com.example.delivery.integration;

import com.example.delivery.RabbitConfig.RabbitBindings;
import com.example.delivery.entity.DeliveryStatusEvent;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface RabbitGateway {

    @Gateway(requestChannel = RabbitBindings.DELIVERY_STATUS)
    void sendDeliveryEvent(DeliveryStatusEvent event);
}
