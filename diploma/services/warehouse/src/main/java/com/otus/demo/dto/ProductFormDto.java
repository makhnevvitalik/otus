package com.otus.demo.dto;

import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductFormDto {

    @NotBlank
    private String title;
    @PositiveOrZero
    private int price;
    @PositiveOrZero
    private int count;
    private Map<String, String> tags = new HashMap<>();
}
