package com.otus.order.service.delivery;

import com.otus.order.service.OrderedProduct;
import java.util.List;
import lombok.Data;

@Data
public class NewDelivery {

    private String orderId;
    private String clientId;
    private String address;
    private List<OrderedProduct> products;
}
