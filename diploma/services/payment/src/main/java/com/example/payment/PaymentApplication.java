package com.example.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@SpringBootApplication
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
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
        if(!template.collectionExists(PaymentLog.class)) {
            template.createCollection(PaymentLog.class);
        }
    }
}
