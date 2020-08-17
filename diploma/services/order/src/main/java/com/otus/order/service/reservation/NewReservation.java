package com.otus.order.service.reservation;

import com.otus.order.domain.ReservedProduct;
import java.util.List;
import lombok.Data;

@Data
public class NewReservation {

    private String clientId;
    private String orderId;
    private List<ReservedProduct> products;
}
