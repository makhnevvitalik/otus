package com.otus.demo.integration;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class WarehouseProduct {

    private String id;
    private String title;
    private int price;
    private int count;
    private Map<String, String> tags = new HashMap<>();
}
