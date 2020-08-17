package com.otus.demo.dto;

import java.util.List;
import lombok.Data;

@Data
public class ReservationFormDto {

    private String clientId;
    private String orderId;
    private List<ReservedProductFormDto> products;

    @Data
    public static class ReservedProductFormDto {

        private String id;
        private int count;
        private int price;
    }
}
