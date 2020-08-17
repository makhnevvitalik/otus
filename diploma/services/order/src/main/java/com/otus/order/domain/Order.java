package com.otus.order.domain;

import java.time.Instant;
import java.util.List;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("orders")
public class Order {

    @Id
    private String id;
    /*@Indexed(unique = true)
    private long number;*/
    private String clientId;
    private String address;
    private Status status;
    private DeliveryStatus deliveryStatus;
    private List<ReservedProduct> products;
    private int amount;
    private Instant createdAt;
    private String hash;
    @Version
    private Long version;
}
