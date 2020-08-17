package com.otus.demo.domain;

import java.time.Instant;
import java.util.List;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("reservations")
public class Reservation {

    @Id
    private String id;
    private String clientId;
    private String orderId;
    private List<Product> products;
    private Instant createdAt;
    private boolean confirmed;
    private Instant shippedAt;
    @Version
    private Long version;
}
