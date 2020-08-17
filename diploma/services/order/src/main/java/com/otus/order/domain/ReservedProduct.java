package com.otus.order.domain;

import lombok.Data;

@Data
public class ReservedProduct {

    private String id;
    private int price;
    private int count;
}
