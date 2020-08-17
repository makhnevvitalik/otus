package com.otus.demo.repo;

import com.otus.demo.domain.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    Reservation getByOrderId(String orderId);
}
