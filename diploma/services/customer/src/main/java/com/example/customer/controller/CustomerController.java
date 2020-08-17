package com.example.customer.controller;

import com.example.customer.entity.Customer;
import com.example.customer.repository.CustomerRepo;
import io.micrometer.core.annotation.Timed;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customers")
@Validated
@Timed("customer.requests")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepo repo;

    @GetMapping("/current")
    public ResponseEntity<Customer> get(@RequestHeader("X-Auth-Request-Email") String email) {
        return repo.findByLogin(email)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/current")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@RequestHeader("X-Auth-Request-Email") String email, @RequestBody @Valid EditForm form) {
        Customer customer = repo.findByLogin(email).orElseGet(() -> new Customer(email));
        customer.setName(form.getName());
        customer.setPhone(form.getPhone());
        customer.setAddress(form.getAddress());
        repo.save(customer);
    }
}
