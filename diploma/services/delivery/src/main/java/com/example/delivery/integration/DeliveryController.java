package com.example.delivery.integration;

import com.example.delivery.RabbitConfig.RabbitBindings;
import com.example.delivery.entity.Delivery;
import com.example.delivery.entity.DeliveryStatus;
import com.example.delivery.entity.DeliveryStatusEvent;
import com.example.delivery.entity.Event;
import com.example.delivery.entity.Product;
import com.example.delivery.repository.DeliveryRepository;
import com.example.delivery.repository.EventRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.ConsumerEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
public class DeliveryController {

    @Bean
    public IntegrationFlow createDeliveryFlow(DeliveryRepository deliveryRepo, EventRepository eventRepo) {
        return IntegrationFlows.from(RabbitBindings.CREATE_DELIVERY)
            .convert(NewDeliveryDto.class)
            .<NewDeliveryDto>handle((newDelivery, headers) -> {
                Delivery delivery = new Delivery();
                delivery.setOrderId(newDelivery.getOrderId());
                delivery.setClientId(newDelivery.getClientId());
                delivery.setAddress(newDelivery.getAddress());
                delivery.setStatus(selectStatus(newDelivery.getAddress()));
                delivery.setProducts(toProducts(newDelivery.getProducts()));
                deliveryRepo.save(delivery);

                DeliveryStatusEvent statusEvent = new DeliveryStatusEvent();
                statusEvent.setOrderId(delivery.getOrderId());
                statusEvent.setClientId(delivery.getClientId());
                statusEvent.setStatus(delivery.getStatus());
                statusEvent.setCreatedAt(Instant.now());
                Event event = new Event();
                event.setPayload(statusEvent);
                eventRepo.save(event);

                return null;
            }, ConsumerEndpointSpec::transactional)
            .get();
    }

    @Bean
    public IntegrationFlow cancelDeliveryFlow(DeliveryRepository deliveryRepo) {
        return IntegrationFlows.from(RabbitBindings.CANCEL_DELIVERY)
            .convert(CancelDelivery.class)
            .<CancelDelivery>handle((cancelDelivery, headers) -> {
                deliveryRepo.deleteByOrderId(cancelDelivery.getOrderId());
                return null;
            })
            .get();
    }

    private List<Product> toProducts(List<ProductDto> productDtoList) {
        return productDtoList.stream()
            .map(p -> {
                Product product = new Product();
                product.setId(p.getId());
                product.setCount(p.getCount());
                return product;
            })
            .collect(Collectors.toList());
    }

    private DeliveryStatus selectStatus(String address) {
        return address.toLowerCase().contains("москва") ? DeliveryStatus.REJECTED : DeliveryStatus.CONFIRMED;
    }

    @Data
    private static class NewDeliveryDto {

        private String orderId;
        private String clientId;
        private String address;
        private List<ProductDto> products;
    }

    @Data
    private static class ProductDto {

        private String id;
        private int count;
    }

    @Data
    private static class CancelDelivery {

        private final String orderId;
    }
}
