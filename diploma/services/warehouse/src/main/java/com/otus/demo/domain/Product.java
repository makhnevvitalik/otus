package com.otus.demo.domain;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("products")
public class Product {

    @Id
    private String id;
    private String title;
    private int price;
    private int count;
    private Map<String, String> tags = new HashMap<>();
    @Version
    private Long version;
}
