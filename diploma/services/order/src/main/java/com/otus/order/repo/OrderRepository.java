package com.otus.order.repo;

import com.otus.order.domain.Order;
import com.otus.order.domain.Status;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    Page<Order> findAllByClientId(String clientId, Pageable pageable);

    Optional<Order> findByClientIdAndStatusAndHash(String clientId, Status status, String hash);
}
