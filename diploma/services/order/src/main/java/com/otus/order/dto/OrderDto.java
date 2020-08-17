package com.otus.order.dto;

import com.otus.order.domain.ReservedProduct;
import com.otus.order.domain.DeliveryStatus;
import com.otus.order.domain.Status;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {

    private String id;
    //private long number;
    private String clientId;
    private String address;
    private Status status;
    private DeliveryStatus deliveryStatus;
    private List<ReservedProduct> products;
    private int amount;
    private Instant createdAt;
}
