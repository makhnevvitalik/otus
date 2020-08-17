package com.otus.order.controller;

import com.otus.order.dto.OrderDto;
import com.otus.order.dto.OrderDtoMapper;
import com.otus.order.dto.PageDto;
import com.otus.order.service.NewOrder;
import com.otus.order.service.OrderService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@Validated
@Timed("order.requests")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;
    private final OrderDtoMapper mapper;

    @GetMapping
    public PageDto<OrderDto> search(
        @RequestHeader("X-Auth-Request-Email") String email,
        Pageable pageable
    ) {
        return mapper.toDto(orderService.search(email, pageable).map(mapper::toDto));
    }

    @PostMapping
    public OrderDto add(
        @RequestHeader("X-Auth-Request-Email") String email,
        @RequestBody NewOrder newOrder
    ) {
        return mapper.toDto(orderService.add(email, newOrder));
    }
}
