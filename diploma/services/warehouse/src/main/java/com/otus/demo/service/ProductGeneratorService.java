package com.otus.demo.service;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import com.github.javafaker.Witcher;
import com.otus.demo.domain.Event;
import com.otus.demo.domain.Product;
import com.otus.demo.repo.EventRepository;
import com.otus.demo.repo.ProductRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductGeneratorService {

    private final Faker faker = new Faker();
    private final MongoTemplate template;
    private final ProductRepository productRepo;
    private final EventRepository eventRepo;

    public void generate(int size) {
        log.info("Starting data generation: {}", size);

        Random random = new Random();
        for (int i = 0; i < size; i++) {
            if (i % 10000 == 0) {
                log.info("Left: {}", size - i);
            }

            Book book = faker.book();
            Witcher witcher = faker.witcher();

            Product product = new Product();
            product.setTitle(book.title());
            product.setPrice(499 + random.nextInt(5000));
            product.setCount(random.nextInt(10000));

            Map<String, String> tags = new HashMap<>();
            tags.put("desc", witcher.quote());
            tags.put("author", book.author());
            product.setTags(tags);
            productRepo.save(product);

            Event event = new Event();
            ProductEvent productEvent = new ProductEvent();
            productEvent.setType(ProductEvent.ProductEventType.ADD);
            productEvent.setProduct(product);
            event.setPayload(productEvent);
            eventRepo.save(event);
        }
        log.info("Done");
    }
}
