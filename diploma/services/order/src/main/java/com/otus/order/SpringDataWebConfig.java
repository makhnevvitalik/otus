package com.otus.order;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.math.BigDecimal;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.method.HandlerMethod;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(SpringDataWebAutoConfiguration.class)
public class SpringDataWebConfig {

    @Bean
    public PageableParameterPlugin pageableParameterPlugin(SpringDataWebProperties properties) {
        return new PageableParameterPlugin(properties);
    }

    @RequiredArgsConstructor
    private static class PageableParameterPlugin implements OperationCustomizer {

        private final SpringDataWebProperties properties;

        static {
            AbstractRequestBuilder.addRequestWrapperToIgnore(Pageable.class, PageRequest.class);
        }

        @Override
        public Operation customize(Operation operation, HandlerMethod handlerMethod) {
            if (hasPageableParameter(handlerMethod)) {
                var pageSchema = new Schema<Integer>().type("integer");
                if (properties.getPageable().isOneIndexedParameters()) {
                    pageSchema.setDefault(1);
                    pageSchema.setMinimum(BigDecimal.ONE);
                } else {
                    pageSchema.setDefault(0);
                    pageSchema.setMinimum(BigDecimal.ZERO);
                }
                var sizeSchema = new Schema<Integer>().type("integer")
                    .minimum(BigDecimal.ONE)
                    .maximum(BigDecimal.valueOf(properties.getPageable().getMaxPageSize()));
                sizeSchema.setDefault(properties.getPageable().getDefaultPageSize());
                operation
                    .addParametersItem(new Parameter()
                        .description("Номер страницы. Отсчет начинается с " + pageSchema.getMinimum())
                        .name(properties.getPageable().getPageParameter())
                        .in("query")
                        .schema(pageSchema))
                    .addParametersItem(new Parameter()
                        .description("Число элементов в странице")
                        .name(properties.getPageable().getSizeParameter())
                        .in("query")
                        .schema(sizeSchema))
                    .addParametersItem(new Parameter()
                        .description("Sorting criteria in the format: property(,asc|desc). "
                            + "Default sort order is ascending. " + "Multiple sort criteria are supported.")
                        .name(properties.getSort().getSortParameter())
                        .in("query")
                        .schema(new Schema<String>().type("string")));
                return operation;
            }
            return operation;
        }

        private static boolean hasPageableParameter(HandlerMethod handlerMethod) {
            return Arrays.stream(handlerMethod.getMethodParameters())
                .anyMatch(methodParameter -> methodParameter.getParameterType().isAssignableFrom(Pageable.class));
        }
    }

}
