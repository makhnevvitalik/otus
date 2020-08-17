package com.otus.demo.integration;

import com.otus.demo.dto.ProductDtoMapper;
import com.otus.demo.repo.EventRepository;
import com.otus.demo.service.ProductEvent;
import com.otus.demo.service.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventIntegration {

    private final EventRepository eventRepo;
    private final RabbitGateway gateway;
    private final ProductDtoMapper productMapper;

    @Scheduled(fixedDelay = 200)
    public void sendEventTask() {
        eventRepo.findAll(Pageable.unpaged()).getContent().forEach(event -> {
            try {
                if (event.getPayload() instanceof ProductEvent) {
                    ProductEvent payload = (ProductEvent) event.getPayload();

                    ProductEventDto eventDto = new ProductEventDto();
                    eventDto.setId(event.getId());
                    eventDto.setType(payload.getType());
                    eventDto.setProduct(productMapper.toDto(payload.getProduct()));
                    gateway.sendProductEvent(eventDto);
                } else if(event.getPayload() instanceof ReservationEvent) {
                    ReservationEvent payload = (ReservationEvent) event.getPayload();

                    ReservationEventDto eventDto = new ReservationEventDto();
                    eventDto.setId(event.getId());
                    eventDto.setType(payload.getType());
                    eventDto.setReservation(productMapper.toDto(payload.getReservation()));
                    gateway.sendReservationEvent(eventDto);
                } else {
                    log.warn("Unknown payload type: " + event.getPayload().getClass().getName());
                    return;
                }
                eventRepo.delete(event);
            } catch (Exception e) {
                log.error(null, e);
            }
        });
    }
}
