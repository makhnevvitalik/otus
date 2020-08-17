package com.example.customer.controller;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditForm {

    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;
}
