package com.example.delivery.integration;

import com.example.delivery.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventIntegration {

    private final EventRepository eventRepo;
    private final RabbitGateway gateway;

    @Scheduled(fixedDelay = 200)
    public void sendEventTask() {
        eventRepo.findAll(Pageable.unpaged()).getContent().forEach(event -> {
            try {
                gateway.sendDeliveryEvent(event.getPayload());
                eventRepo.delete(event);
            } catch (Exception e) {
                log.error(null, e);
            }
        });
    }
}
