package com.otus.demo.controller;

import com.otus.demo.dto.ProductDtoMapper;
import com.otus.demo.dto.ReservationDto;
import com.otus.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/warehouse/reservations")
@Validated
@RequiredArgsConstructor
public class ReservationController {

    private final ProductService productService;
    private final ProductDtoMapper mapper;

    @GetMapping("/{orderId}")
    public ReservationDto get(@PathVariable String orderId) {
        return mapper.toDto(productService.getReservation(orderId));
    }
}
