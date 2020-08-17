package com.otus.order.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("order_numbers")
public class OrderNumber {

    public static final String ID = "COUNTER";

    @Id
    private String id = ID;
    private long number;
    @Version
    private Long version;

    public long incrementAndGet() {
        number++;
        return number;
    }
}
