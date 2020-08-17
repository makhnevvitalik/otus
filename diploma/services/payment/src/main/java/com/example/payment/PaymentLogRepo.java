package com.example.payment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentLogRepo extends MongoRepository<PaymentLog, String> {
}
