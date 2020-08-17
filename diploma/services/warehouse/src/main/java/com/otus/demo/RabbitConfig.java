package com.otus.demo;

import com.otus.demo.RabbitConfig.RabbitBindings;
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

        String PRODUCT_UPDATE = "productUpdate";
        String RESERVATION_UPDATE = "reservationUpdate";

        String CREATE_RESERVATION = "createReservation";
        String CANCEL_RESERVATION = "cancelReservation";
        String CONFIRM_RESERVATION = "confirmReservation";

        @Output(PRODUCT_UPDATE)
        MessageChannel productUpdate();

        @Output(RESERVATION_UPDATE)
        MessageChannel reservationUpdate();

        @Input(CREATE_RESERVATION)
        MessageChannel createReservation();

        @Input(CANCEL_RESERVATION)
        MessageChannel cancelReservation();

        @Input(CONFIRM_RESERVATION)
        MessageChannel confirmReservation();
    }
}
