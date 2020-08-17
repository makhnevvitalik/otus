package com.otus.demo.integration;

import com.otus.demo.dto.ProductDto;
import com.otus.demo.service.ProductEvent.ProductEventType;
import lombok.Data;

@Data
public class ProductEventDto {

    private String id;
    private ProductEventType type;
    private ProductDto product;
}
