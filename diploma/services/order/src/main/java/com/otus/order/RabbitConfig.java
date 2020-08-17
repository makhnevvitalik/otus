package com.otus.order;

import com.otus.order.RabbitConfig.RabbitBindings;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableBinding(RabbitBindings.class)
@PropertySource("classpath:/rabbit.properties")
public class RabbitConfig {

    public interface RabbitBindings {

        String CREATE_PAYMENT = "createPayment";
        String CANCEL_PAYMENT = "cancelPayment";
        String PAYMENT_STATUS = "paymentStatus";
        String CREATE_RESERVATION = "createReservation";
        String CONFIRM_RESERVATION = "confirmReservation";
        String CANCEL_RESERVATION = "cancelReservation";
        String RESERVATION_STATUS = "reservationStatus";
        String CREATE_DELIVERY = "createDelivery";
        String CANCEL_DELIVERY = "cancelDelivery";
        String DELIVERY_STATUS = "deliveryStatus";

        @Output(CREATE_PAYMENT)
        MessageChannel createPayment();

        @Output(CANCEL_PAYMENT)
        MessageChannel cancelPayment();

        @Input(PAYMENT_STATUS)
        MessageChannel paymentStatus();

        @Output(CREATE_RESERVATION)
        MessageChannel createReservation();

        @Output(CONFIRM_RESERVATION)
        MessageChannel confirmReservation();

        @Output(CANCEL_RESERVATION)
        MessageChannel cancelReservation();

        @Input(RESERVATION_STATUS)
        MessageChannel reservationStatus();

        @Output(CREATE_DELIVERY)
        MessageChannel createDelivery();

        @Output(CANCEL_DELIVERY)
        MessageChannel cancelDelivery();

        @Input(DELIVERY_STATUS)
        MessageChannel deliveryStatus();
    }
}
