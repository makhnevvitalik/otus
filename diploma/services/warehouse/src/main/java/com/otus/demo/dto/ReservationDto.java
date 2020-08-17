package com.otus.demo.dto;

import java.util.List;
import lombok.Data;

@Data
public class ReservationDto {

    private String id;
    private String clientId;
    private String orderId;
    private List<ReservedProduct> products;

    @Data
    public static class ReservedProduct {

        private String id;
        private String title;
        private int price;
        private int count;
    }
}
