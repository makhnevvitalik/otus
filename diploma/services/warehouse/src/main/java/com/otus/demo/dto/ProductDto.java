package com.otus.demo.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ProductDto {

    private String id;
    private String title;
    private int price;
    private int count;
    private Map<String, String> tags = new HashMap<>();
}
