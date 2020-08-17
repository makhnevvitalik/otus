package com.otus.order.integration;

import com.otus.order.RabbitConfig.RabbitBindings;
import com.otus.order.service.delivery.CancelDelivery;
import com.otus.order.service.delivery.NewDelivery;
import com.otus.order.service.payment.CancelPayment;
import com.otus.order.service.payment.NewPayment;
import com.otus.order.service.reservation.CancelReservation;
import com.otus.order.service.reservation.ConfirmReservation;
import com.otus.order.service.reservation.NewReservation;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface RabbitGateway {

    @Gateway(requestChannel = RabbitBindings.CREATE_DELIVERY)
    void createDelivery(NewDelivery request);

    @Gateway(requestChannel = RabbitBindings.CANCEL_DELIVERY)
    void cancelDelivery(CancelDelivery request);

    @Gateway(requestChannel = RabbitBindings.CREATE_PAYMENT)
    void createPayment(NewPayment request);

    @Gateway(requestChannel = RabbitBindings.CANCEL_PAYMENT)
    void cancelPayment(CancelPayment request);

    @Gateway(requestChannel = RabbitBindings.CREATE_RESERVATION)
    void createReservation(NewReservation request);

    @Gateway(requestChannel = RabbitBindings.CONFIRM_RESERVATION)
    void confirmReservation(ConfirmReservation request);

    @Gateway(requestChannel = RabbitBindings.CANCEL_RESERVATION)
    void cancelReservation(CancelReservation request);
}
