package com.otus.demo.integration;

import lombok.Data;

@Data
public class WarehouseProductEvent {

    private String id;
    private EventType type;
    private WarehouseProduct product;

    public enum EventType {
        ADD,
        UPDATE,
        DELETE
    }
}
