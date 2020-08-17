package com.otus.demo.integration;

import com.otus.demo.RabbitConfig.RabbitBindings;
import com.otus.demo.domain.Product;
import com.otus.demo.integration.WarehouseProductEvent.EventType;
import com.otus.demo.service.ProductService;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.RouterSpec;
import org.springframework.integration.router.MethodInvokingRouter;

@Configuration
@RequiredArgsConstructor
public class WarehouseListener {

    private final ProductService service;

    @Bean
    public IntegrationFlow productListener() {
        return IntegrationFlows.from(RabbitBindings.PRODUCT_UPDATE)
            .convert(WarehouseProductEvent.class)
            .route(WarehouseProductEvent::getType, (Consumer<RouterSpec<EventType, MethodInvokingRouter>>) spec -> spec
                .subFlowMapping(EventType.ADD, addProductFlow())
                .subFlowMapping(EventType.UPDATE, updateProductFlow())
                .subFlowMapping(EventType.DELETE, deleteProductFlow()))
            .get();
    }

    private IntegrationFlow addProductFlow() {
        return f -> f
            .transform(WarehouseProductEvent::getProduct)
            .<WarehouseProduct>handle((warehouseProduct, h) -> {
                Product product = new Product();
                product.setId(warehouseProduct.getId());
                product.setTitle(warehouseProduct.getTitle());
                product.setPrice(warehouseProduct.getPrice());
                product.setCount(warehouseProduct.getCount());
                product.setTags(warehouseProduct.getTags());
                service.save(product);
                return null;
            });
    }

    private IntegrationFlow updateProductFlow() {
        return f -> f
            .transform(WarehouseProductEvent::getProduct)
            .<WarehouseProduct>handle((warehouseProduct, h) -> {
                Product product = service.get(warehouseProduct.getId());
                if (product == null) {
                    product = new Product();
                    product.setId(warehouseProduct.getId());
                }
                product.setTitle(warehouseProduct.getTitle());
                product.setPrice(warehouseProduct.getPrice());
                product.setCount(warehouseProduct.getCount());
                product.setTags(warehouseProduct.getTags());
                service.save(product);

                return null;
            });
    }

    private IntegrationFlow deleteProductFlow() {
        return f -> f
            .transform(WarehouseProductEvent::getProduct)
            .<WarehouseProduct>handle((product, h) -> {
                service.delete(product.getId());
                return null;
            });
    }
}
