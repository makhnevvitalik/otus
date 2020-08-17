package com.example.payment;

import com.example.payment.RabbitConfig.RabbitBindings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
public class AmqpController {

    @Bean
    public IntegrationFlow createPaymentFlow(PaymentLogRepo repo) {
        return IntegrationFlows.from(RabbitBindings.CREATE_PAYMENT)
            .convert(NewPayment.class)
            .<NewPayment>handle((newPayment, headers) -> {
                PaymentLog payment = new PaymentLog();
                payment.setOrderId(newPayment.getOrderId());
                payment.setStatus("New payment: " + newPayment.getAmount());
                repo.save(payment);
                return null;
            })
            .get();
    }


    @Bean
    public IntegrationFlow cancelPaymentFlow(PaymentLogRepo repo) {
        return IntegrationFlows.from(RabbitBindings.CANCEL_PAYMENT)
            .convert(CancelPayment.class)
            .<CancelPayment>handle((cancelPayment, headers) -> {
                PaymentLog payment = new PaymentLog();
                payment.setOrderId(cancelPayment.getOrderId());
                payment.setStatus("Return money");
                repo.save(payment);
                return null;
            })
            .get();
    }
}
