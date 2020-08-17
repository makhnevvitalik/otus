package com.example.delivery;

import com.example.delivery.RabbitConfig.RabbitBindings;
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

        String CREATE_DELIVERY = "createDelivery";
        String CANCEL_DELIVERY = "cancelDelivery";
        String DELIVERY_STATUS = "deliveryStatus";

        @Input(CREATE_DELIVERY)
        MessageChannel createDelivery();

        @Input(CANCEL_DELIVERY)
        MessageChannel cancelDelivery();

        @Output(DELIVERY_STATUS)
        MessageChannel deliveryStatus();
    }
}
