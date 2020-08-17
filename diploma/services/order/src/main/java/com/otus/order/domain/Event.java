package com.otus.order.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("events")
public class Event {

    @Id
    private String id;
    private Object payload;
    private boolean delay;
}
