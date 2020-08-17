package com.otus.order.integration;

import com.otus.order.repo.EventRepository;
import com.otus.order.service.delivery.CancelDelivery;
import com.otus.order.service.delivery.NewDelivery;
import com.otus.order.service.payment.CancelPayment;
import com.otus.order.service.payment.NewPayment;
import com.otus.order.service.reservation.CancelReservation;
import com.otus.order.service.reservation.ConfirmReservation;
import com.otus.order.service.reservation.NewReservation;
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

    @Scheduled(fixedDelay = 200)
    public void sendEventTask() {
        eventRepo.findAll(Pageable.unpaged()).getContent().forEach(event -> {
            try {
                if(event.isDelay()) {
                    Thread.sleep(2000);
                }

                if (event.getPayload() instanceof NewPayment) {
                    gateway.createPayment((NewPayment) event.getPayload());
                } else if (event.getPayload() instanceof CancelPayment) {
                    gateway.cancelPayment((CancelPayment) event.getPayload());
                } else if (event.getPayload() instanceof NewReservation) {
                    gateway.createReservation((NewReservation) event.getPayload());
                } else if (event.getPayload() instanceof ConfirmReservation) {
                    gateway.confirmReservation((ConfirmReservation) event.getPayload());
                } else if (event.getPayload() instanceof CancelReservation) {
                    gateway.cancelReservation((CancelReservation) event.getPayload());
                } else if (event.getPayload() instanceof NewDelivery) {
                    gateway.createDelivery((NewDelivery) event.getPayload());
                } else if (event.getPayload() instanceof CancelDelivery) {
                    gateway.cancelDelivery((CancelDelivery) event.getPayload());
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
