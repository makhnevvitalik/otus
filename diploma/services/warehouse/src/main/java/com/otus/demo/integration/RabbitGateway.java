package com.otus.demo.integration;

import com.otus.demo.RabbitConfig.RabbitBindings;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface RabbitGateway {

    @Gateway(requestChannel = RabbitBindings.PRODUCT_UPDATE)
    void sendProductEvent(ProductEventDto event);

    @Gateway(requestChannel = RabbitBindings.RESERVATION_UPDATE)
    void sendReservationEvent(ReservationEventDto event);
}
