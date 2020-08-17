package com.example.delivery.controller;

import com.example.delivery.entity.Delivery;
import com.example.delivery.entity.DeliveryStatus;
import com.example.delivery.entity.DeliveryStatusEvent;
import com.example.delivery.entity.Event;
import com.example.delivery.repository.DeliveryRepository;
import com.example.delivery.repository.EventRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryRestController {

    private final DeliveryRepository deliveryRepo;
    private final EventRepository eventRepo;

    @GetMapping
    public Page<Delivery> getPage() {
        return deliveryRepo.findAll(Pageable.unpaged());
    }

    @PostMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void changeStatus(@PathVariable String orderId, @RequestParam DeliveryStatus status) {
        Delivery delivery = deliveryRepo.getByOrderId(orderId);
        delivery.setStatus(status);
        deliveryRepo.save(delivery);

        DeliveryStatusEvent statusEvent = new DeliveryStatusEvent();
        statusEvent.setOrderId(delivery.getOrderId());
        statusEvent.setClientId(delivery.getClientId());
        statusEvent.setStatus(delivery.getStatus());
        statusEvent.setCreatedAt(Instant.now());
        Event event = new Event();
        event.setPayload(statusEvent);
        eventRepo.save(event);
    }
}
