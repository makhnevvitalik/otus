package com.example.customer.controller;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddForm {

    @NotBlank
    private String login;
}
