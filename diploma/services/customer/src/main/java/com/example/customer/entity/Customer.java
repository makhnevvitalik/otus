package com.example.customer.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("customers")
public class Customer {

    @Id
    private String id;
    @Indexed(unique = true)
    @Setter(AccessLevel.NONE)
    private String login;
    private String name;
    private String phone;
    private String address;

    public Customer(String login) {
        this.login = login;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }
}
