package com.example.customer.repository;

import com.example.customer.entity.Customer;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends MongoRepository<Customer, String> {

    Optional<Customer> findByLogin(String clientId);
}
