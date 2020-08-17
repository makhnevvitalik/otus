package com.example.payment;

import com.example.payment.RabbitConfig.RabbitBindings;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface RabbitGateway {

    @Gateway(requestChannel = RabbitBindings.PAYMENT_STATUS)
    void sendPaymentEvent(PaymentStatusEvent event);
}
