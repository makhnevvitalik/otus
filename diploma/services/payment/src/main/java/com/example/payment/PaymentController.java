package com.example.payment;

import static com.example.payment.PaymentStatusEvent.PaymentStatus;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RabbitGateway rabbitGateway;
    private final PaymentLogRepo repo;

    @GetMapping
    public List<PaymentLog> payments() {
        return repo.findAll();
    }

    @PostMapping("/{orderId}/pay")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void makePayment(
        @PathVariable String orderId,
        @RequestParam(required = false, defaultValue = "OK") PaymentStatus status) {

        PaymentLog payment = new PaymentLog();
        payment.setOrderId(orderId);
        payment.setStatus("Payment " + status.name().toLowerCase());
        repo.save(payment);

        rabbitGateway.sendPaymentEvent(new PaymentStatusEvent(orderId, status));
    }
}
