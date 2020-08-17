package com.otus.order;

import com.otus.order.domain.Event;
import com.otus.order.domain.Order;
import com.otus.order.domain.OrderNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@EnableRetry
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        createDb(event.getApplicationContext());

        Environment env = event.getApplicationContext().getEnvironment();
        String port = env.getProperty("server.port");
        log.info("Swagger UI: http://localhost:{}/swagger-ui.html", port);
        log.info("Mongo url: {}", env.getProperty("spring.data.mongodb.uri"));
    }

    private static void createDb(ApplicationContext context) {
        MongoTemplate template = context.getBean(MongoTemplate.class);
        if(!template.collectionExists(Event.class)) {
            template.createCollection(Event.class);
        }
        if(!template.collectionExists(Order.class)) {
            template.createCollection(Order.class);
        }
        if(!template.collectionExists(OrderNumber.class)) {
            template.createCollection(OrderNumber.class);
        }
    }
}