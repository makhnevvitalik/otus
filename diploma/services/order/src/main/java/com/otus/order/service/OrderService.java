package com.otus.order.service;

import com.otus.order.client.warehouse.Product;
import com.otus.order.client.warehouse.WarehouseClient;
import com.otus.order.domain.Event;
import com.otus.order.domain.Order;
import com.otus.order.domain.ReservedProduct;
import com.otus.order.domain.Status;
import com.otus.order.repo.EventRepository;
import com.otus.order.repo.OrderNumberRepository;
import com.otus.order.repo.OrderRepository;
import com.otus.order.service.delivery.CancelDelivery;
import com.otus.order.service.delivery.DeliveryStatusEvent;
import com.otus.order.service.delivery.DeliveryStatusEvent.DeliveryStatus;
import com.otus.order.service.delivery.NewDelivery;
import com.otus.order.service.payment.CancelPayment;
import com.otus.order.service.payment.NewPayment;
import com.otus.order.service.payment.PaymentEvent;
import com.otus.order.service.payment.PaymentEvent.PaymentStatus;
import com.otus.order.service.reservation.CancelReservation;
import com.otus.order.service.reservation.ConfirmReservation;
import com.otus.order.service.reservation.NewReservation;
import com.otus.order.service.reservation.Reservation;
import com.otus.order.service.reservation.ReservationEvent;
import com.otus.order.service.reservation.ReservationEvent.ReservationEventType;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderNumberRepository orderNumberRepo;
    private final EventRepository eventRepo;
    private final WarehouseClient warehouseClient;

    public Page<Order> search(String clientId, Pageable pageable) {
        return orderRepo.findAllByClientId(clientId, pageable);
    }

    @Transactional
    public Order add(String email, NewOrder newOrder) {
        String orderHash = hash(email, newOrder);
        Optional<Order> persistedOrder = orderRepo
            .findByClientIdAndStatusAndHash(email, Status.NEW, orderHash);
        if (persistedOrder.isPresent()) {
            return persistedOrder.get();
        }

        List<Product> actualProducts = warehouseClient
            .get(newOrder.getProducts().stream().map(OrderedProduct::getId).collect(Collectors.toList()));
        List<ReservedProduct> products = toReservedProducts(newOrder.getProducts(), actualProducts);
        int totalAmount = products.stream()
            .mapToInt(p -> p.getPrice() * p.getCount())
            .sum();

        /*OrderNumber orderNumberEntity = orderNumberRepo.get();
        long number = orderNumberEntity.incrementAndGet();
        orderNumberRepo.save(orderNumberEntity);*/

        Order order = new Order();
        //order.setNumber(number);
        order.setClientId(email);
        order.setAddress(newOrder.getAddress());
        order.setStatus(Status.NEW);
        order.setProducts(products);
        order.setCreatedAt(Instant.now());
        order.setAmount(totalAmount);
        order.setHash(orderHash);
        orderRepo.save(order);

        createReservation(order, newOrder.isDelay());
        return order;
    }

    @Transactional
    public void updateReservationStatus(ReservationEvent reservationEvent) {
        Reservation reservation = reservationEvent.getReservation();
        Order order = orderRepo.findById(reservation.getOrderId()).orElseThrow();
        if (order.getStatus() == Status.CANCELED || order.getStatus() == Status.RESERVED) {
            return;
        }

        if (reservationEvent.getType() == ReservationEventType.OK) {
            order.setStatus(Status.RESERVED);
            createPayment(order);
        } else {
            order.setStatus(Status.CANCELED);
        }
        orderRepo.save(order);
    }

    @Transactional
    public void updatePaymentInfo(PaymentEvent paymentEvent) {
        Order order = orderRepo.findById(paymentEvent.getOrderId()).orElseThrow();
        if (order.getStatus() == Status.CANCELED || order.getStatus() == Status.PAID) {
            return;
        }

        if (paymentEvent.getStatus() == PaymentStatus.OK) {
            order.setStatus(Status.PAID);
            createDelivery(order);
        } else {
            order.setStatus(Status.CANCELED);
            cancelReservation(order.getId());
        }
        orderRepo.save(order);
    }

    @Transactional
    public void updateDeliveryStatus(DeliveryStatusEvent statusEvent) {
        Order order = orderRepo.findById(statusEvent.getOrderId()).orElseThrow();
        if (order.getStatus() == Status.CANCELED || order.getStatus() == Status.COMPLETED) {
            return;
        }

        if (statusEvent.getStatus() == DeliveryStatus.REJECTED) {
            cancelReservation(order.getId());
            cancelPayment(order.getId());

            order.setStatus(Status.CANCELED);
            order.setDeliveryStatus(null);
            orderRepo.save(order);
            return;
        } else if (statusEvent.getStatus() == DeliveryStatus.CONFIRMED) {
            order.setStatus(Status.DELIVERY);
        } else if (statusEvent.getStatus() == DeliveryStatus.DELIVERED) {
            order.setStatus(Status.COMPLETED);
        }
        order.setDeliveryStatus(com.otus.order.domain.DeliveryStatus.valueOf(statusEvent.getStatus().name()));
        orderRepo.save(order);
    }

    private void createDelivery(Order order) {
        NewDelivery newDelivery = new NewDelivery();
        newDelivery.setOrderId(order.getId());
        newDelivery.setClientId(order.getClientId());
        newDelivery.setAddress(order.getAddress());
        newDelivery.setProducts(toOrderedProducts(order.getProducts()));

        Event event = new Event();
        event.setPayload(newDelivery);
        eventRepo.save(event);
    }

    private void cancelDelivery(String orderId) {
        Event event = new Event();
        event.setPayload(new CancelDelivery(orderId));
        eventRepo.save(event);
    }

    private void createReservation(Order order, boolean delay) {
        NewReservation newReservation = new NewReservation();
        newReservation.setOrderId(order.getId());
        newReservation.setClientId(order.getClientId());
        newReservation.setProducts(order.getProducts());

        Event event = new Event();
        event.setPayload(newReservation);
        event.setDelay(delay);
        eventRepo.save(event);
    }

    private void confirmReservation(String orderId) {
        Event event = new Event();
        event.setPayload(new ConfirmReservation(orderId));
        eventRepo.save(event);
    }

    private void cancelReservation(String orderId) {
        Event event = new Event();
        event.setPayload(new CancelReservation(orderId));
        eventRepo.save(event);
    }

    private void createPayment(Order order) {
        NewPayment newPayment = new NewPayment();
        newPayment.setOrderId(order.getId());
        newPayment.setClientId(order.getClientId());
        newPayment.setAmount(order.getAmount());

        Event event = new Event();
        event.setPayload(newPayment);
        eventRepo.save(event);
    }

    private void cancelPayment(String orderId) {
        Event event = new Event();
        event.setPayload(new CancelPayment(orderId));
        eventRepo.save(event);
    }

    private static String hash(String email, NewOrder order) {
        StringBuilder builder = new StringBuilder();
        builder.append(email);
        builder.append(order.getAddress());
        order.getProducts().forEach(p -> builder.append(p.getId()).append(p.getCount()));
        return DigestUtils.sha256Hex(builder.toString());
    }

    private static List<ReservedProduct> toReservedProducts(
        List<OrderedProduct> products,
        List<Product> actualProducts) {

        Map<String, Product> actualProductMap = actualProducts.stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));
        return products.stream()
            .map(p -> {
                ReservedProduct product = new ReservedProduct();
                product.setId(p.getId());
                product.setPrice(actualProductMap.get(p.getId()).getPrice());
                product.setCount(p.getCount());
                return product;
            })
            .collect(Collectors.toList());
    }

    private static List<OrderedProduct> toOrderedProducts(List<ReservedProduct> products) {
        return products.stream()
            .map(p -> {
                OrderedProduct product = new OrderedProduct();
                product.setId(p.getId());
                product.setCount(p.getCount());
                return product;
            })
            .collect(Collectors.toList());
    }
}
