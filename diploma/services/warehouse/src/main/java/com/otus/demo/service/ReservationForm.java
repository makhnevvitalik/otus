package com.otus.demo.service;

import java.util.List;
import lombok.Data;

@Data
public class ReservationForm {

    private String clientId;
    private String orderId;
    private List<ReservedProductForm> products;

    @Data
    public static class ReservedProductForm {

        private String id;
        private int count;
        private int price;
    }
}
