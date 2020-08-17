package com.example.payment;

import com.example.payment.RabbitConfig.RabbitBindings;
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

        @Input(CREATE_PAYMENT)
        MessageChannel createPayment();

        @Input(CANCEL_PAYMENT)
        MessageChannel cancelPayment();

        @Output(PAYMENT_STATUS)
        MessageChannel paymentStatus();
    }
}
