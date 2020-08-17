package com.otus.order.repo;

import com.otus.order.domain.OrderNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderNumberRepository extends MongoRepository<OrderNumber, String> {

    default OrderNumber get() {
        return findById(OrderNumber.ID).orElseGet(() -> {
            OrderNumber number = new OrderNumber();
            number.setId(OrderNumber.ID);
            return number;
        });
    }
}
