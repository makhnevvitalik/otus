package com.example.payment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("payments")
public class PaymentLog {

    @Id
    private String id;
    private String orderId;
    private String status;
}
