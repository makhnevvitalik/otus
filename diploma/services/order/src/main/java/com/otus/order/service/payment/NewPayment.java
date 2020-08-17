package com.otus.order.service.payment;

import lombok.Data;

@Data
public class NewPayment {

    private String clientId;
    private String orderId;
    private int amount;
}
