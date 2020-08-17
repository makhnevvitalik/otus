package com.otus.demo.integration;

import com.otus.demo.RabbitConfig.RabbitBindings;
import com.otus.demo.dto.ProductDtoMapper;
import com.otus.demo.dto.ReservationFormDto;
import com.otus.demo.service.ProductService;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
public class AmqpController {

    @Bean
    public IntegrationFlow createReservationFlow(ProductService service, ProductDtoMapper mapper) {
        return IntegrationFlows.from(RabbitBindings.CREATE_RESERVATION)
            .convert(ReservationFormDto.class)
            .<ReservationFormDto>handle((reservationForm, headers) -> {
                service.makeReservation(mapper.fromDto(reservationForm));
                return null;
            })
            .get();
    }

    @Bean
    public IntegrationFlow confirmReservationFlow(ProductService service) {
        return IntegrationFlows.from(RabbitBindings.CONFIRM_RESERVATION)
            .convert(ConfirmReservation.class)
            .<ConfirmReservation>handle((confirmReservation, headers) -> {
                service.confirmReservation(confirmReservation.getOrderId());
                return null;
            })
            .get();
    }

    @Bean
    public IntegrationFlow cancelReservationFlow(ProductService service) {
        return IntegrationFlows.from(RabbitBindings.CANCEL_RESERVATION)
            .convert(CancelReservation.class)
            .<CancelReservation>handle((cancelReservation, headers) -> {
                service.cancelReservation(cancelReservation.getOrderId());
                return null;
            })
            .get();
    }

    @Data
    private static class CancelReservation {

        private final String orderId;
    }

    @Data
    private static class ConfirmReservation {

        private final String orderId;
    }
}
