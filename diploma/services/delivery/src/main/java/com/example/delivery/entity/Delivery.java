package com.example.delivery.entity;

import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("deliveries")
public class Delivery {

    @Id
    private String id;
    @Indexed(unique = true)
    private String orderId;
    private String clientId;
    private String address;
    private List<Product> products;
    private DeliveryStatus status;
    @Version
    private Long version;
}
