package com.otus.demo.service;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ProductForm {

    private String title;
    private int price;
    private int count;
    private Map<String, String> tags = new HashMap<>();
}
