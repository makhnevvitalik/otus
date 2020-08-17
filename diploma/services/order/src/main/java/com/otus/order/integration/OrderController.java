package com.otus.order.integration;

import com.otus.order.RabbitConfig.RabbitBindings;
import com.otus.order.repo.EventRepository;
import com.otus.order.service.delivery.DeliveryStatusEvent;
import com.otus.order.service.OrderService;
import com.otus.order.service.payment.PaymentEvent;
import com.otus.order.service.reservation.ReservationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
public class OrderController {

    @Bean
    public IntegrationFlow updateDeliveryStatus(OrderService orderService, EventRepository eventRepo) {
        return IntegrationFlows.from(RabbitBindings.DELIVERY_STATUS)
            .convert(DeliveryStatusEvent.class)
            .<DeliveryStatusEvent>handle((event, headers) -> {
                orderService.updateDeliveryStatus(event);
                return null;
            })
            .get();
    }

    @Bean
    public IntegrationFlow updatePaymentStatus(OrderService orderService, EventRepository eventRepo) {
        return IntegrationFlows.from(RabbitBindings.PAYMENT_STATUS)
            .convert(PaymentEvent.class)
            .<PaymentEvent>handle((event, headers) -> {
                orderService.updatePaymentInfo(event);
                return null;
            })
            .get();
    }

    @Bean
    public IntegrationFlow updateReservationStatus(OrderService orderService, EventRepository eventRepo) {
        return IntegrationFlows.from(RabbitBindings.RESERVATION_STATUS)
            .convert(ReservationEvent.class)
            .<ReservationEvent>handle((event, headers) -> {
                orderService.updateReservationStatus(event);
                return null;
            })
            .get();
    }
}
