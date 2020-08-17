package com.otus.demo;

import com.otus.demo.domain.Event;
import com.otus.demo.domain.Product;
import com.otus.demo.domain.Reservation;
import com.otus.demo.service.ProductGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class WarehouseApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(WarehouseApplication.class, args);
        Boolean generator = ctx.getEnvironment().getProperty("generator-enabled", Boolean.class, false);
        if (generator) {
            Integer count = ctx.getEnvironment().getProperty("generator-count", Integer.class, 100);
            ctx.getBean(ProductGeneratorService.class).generate(count);
            if (!ctx.getEnvironment().acceptsProfiles(Profiles.of("local"))) {
                ctx.close();
            }
        }
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
        if(!template.collectionExists(Product.class)) {
            template.createCollection(Product.class);
        }
        if(!template.collectionExists(Reservation.class)) {
            template.createCollection(Reservation.class);
        }
    }
}