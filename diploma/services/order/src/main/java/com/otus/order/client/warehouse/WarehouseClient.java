package com.otus.order.client.warehouse;

import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WarehouseClient {

    private static final String BASE_URL = "http://otus-warehouse-service:8080/v1/warehouse/products?id={id}";
    //private static final String BASE_URL = "http://localhost:8083/v1/warehouse/products?id={id}";

    private final RestTemplate template = new RestTemplate();

    public List<Product> get(List<String> ids) {
        String idList = String.join(",", ids);
        ParameterizedTypeReference<List<Product>> responseType = new ParameterizedTypeReference<>() {};
        return template.exchange(BASE_URL, HttpMethod.GET, null, responseType, Map.of("id", idList)).getBody();
    }
}
