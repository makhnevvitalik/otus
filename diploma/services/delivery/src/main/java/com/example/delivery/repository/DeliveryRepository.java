package com.example.delivery.repository;

import com.example.delivery.entity.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends MongoRepository<Delivery, String> {

    Delivery getByOrderId(String orderId);

    void deleteByOrderId(String orderId);
}
