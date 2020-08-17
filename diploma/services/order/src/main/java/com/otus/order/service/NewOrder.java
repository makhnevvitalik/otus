package com.otus.order.service;

import java.util.List;
import lombok.Data;

@Data
public class NewOrder {

    private List<OrderedProduct> products;
    private String address;
    private boolean delay;
}
