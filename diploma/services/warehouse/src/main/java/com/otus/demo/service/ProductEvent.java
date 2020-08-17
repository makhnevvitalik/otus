package com.otus.demo.service;

import com.otus.demo.domain.Product;
import lombok.Data;

@Data
public class ProductEvent {

    private ProductEventType type;
    private Product product;

    public enum ProductEventType {
        ADD,
        UPDATE,
        DELETE
    }
}
