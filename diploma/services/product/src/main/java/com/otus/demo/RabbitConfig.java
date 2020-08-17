package com.otus.demo;

import com.otus.demo.RabbitConfig.RabbitBindings;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableBinding(RabbitBindings.class)
@PropertySource("classpath:/rabbit.properties")
public class RabbitConfig {

    public interface RabbitBindings {

        String PRODUCT_UPDATE = "productUpdate";

        @Input(PRODUCT_UPDATE)
        MessageChannel productUpdate();
    }
}
