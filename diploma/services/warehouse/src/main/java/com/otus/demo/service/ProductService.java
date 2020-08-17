package com.otus.demo.service;

import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import com.otus.demo.domain.Event;
import com.otus.demo.domain.Product;
import com.otus.demo.domain.Reservation;
import com.otus.demo.repo.EventRepository;
import com.otus.demo.repo.ProductRepository;
import com.otus.demo.repo.ReservationRepository;
import com.otus.demo.service.ProductEvent.ProductEventType;
import com.otus.demo.service.ReservationEvent.ReservationEventType;
import com.otus.demo.service.ReservationForm.ReservedProductForm;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private static final QueryConversionPipeline PIPELINE = QueryConversionPipeline.defaultPipeline();
    private static final MongoVisitor VISITOR = new MongoVisitor();
    private final ProductMapper productMapper;

    private final MongoTemplate template;
    private final ProductRepository productRepo;
    private final ReservationRepository reservationRepo;
    private final EventRepository eventRepo;

    public Product get(String id) {
        return productRepo.findById(id).orElseThrow();
    }

    public List<Product> get(List<String> ids) {
        Iterable<Product> products = productRepo.findAllById(ids);
        List<Product> productList = new ArrayList<>();
        products.forEach(productList::add);
        return productList;
    }

    public Page<Product> search(String rsql, Pageable pageable) {
        Criteria criteria;
        if (StringUtils.isEmpty(rsql)) {
            criteria = new Criteria();
        } else {
            criteria = PIPELINE.apply(rsql, ProductSearch.class).query(VISITOR);
        }
        List<Product> list = template.find(Query.query(criteria).with(pageable), Product.class);
        return PageableExecutionUtils.getPage(list, pageable,
            () -> template.count(Query.query(criteria), Product.class));
    }

    @Transactional
    public Product add(ProductForm form) {
        Product product = new Product();
        productMapper.updateProduct(product, form);
        productRepo.save(product);

        saveProductEvent(ProductEventType.ADD, product);
        return product;
    }

    @Transactional
    public void update(String id, ProductForm form) {
        Product product = get(id);
        productMapper.updateProduct(product, form);
        productRepo.save(product);

        saveProductEvent(ProductEventType.UPDATE, product);
    }

    @Transactional
    public void delete(String id) {
        productRepo.deleteById(id);

        Product product = new Product();
        product.setId(id);
        saveProductEvent(ProductEventType.DELETE, product);
    }

    public Reservation getReservation(String orderId) {
        return reservationRepo.getByOrderId(orderId);
    }

    @Transactional
    public void makeReservation(ReservationForm form) {
        Map<String, Integer> reservedProductMap = form.getProducts().stream()
            .collect(Collectors.toMap(ReservedProductForm::getId, ReservedProductForm::getCount));

        Iterable<Product> products = productRepo.findAllById(reservedProductMap.keySet());
        for (Product product : products) {
            int count = product.getCount() - reservedProductMap.get(product.getId());
            if (count < 0) {
                Reservation reservation = new Reservation();
                reservation.setOrderId(form.getOrderId());
                reservation.setClientId(form.getClientId());
                saveReservationEvent(ReservationEventType.REJECTED, reservation);
                return;
            }
        }

        products.forEach(product -> {
            product.setCount(product.getCount() - reservedProductMap.get(product.getId()));
            productRepo.save(product);
            saveProductEvent(ProductEventType.UPDATE, product);
        });

        List<Product> reservedProducts = form.getProducts().stream()
            .map(p -> {
                Product product = productRepo.findById(p.getId()).orElseThrow();
                product.setCount(p.getCount());
                product.setPrice(p.getPrice());
                return product;
            })
            .collect(Collectors.toList());
        Reservation reservation = new Reservation();
        reservation.setClientId(form.getClientId());
        reservation.setOrderId(form.getOrderId());
        reservation.setProducts(reservedProducts);
        reservation.setCreatedAt(Instant.now());
        reservationRepo.save(reservation);
        saveReservationEvent(ReservationEventType.OK, reservation);
    }

    public void confirmReservation(String orderId) {
        Reservation reservation = reservationRepo.getByOrderId(orderId);
        reservation.setConfirmed(true);
        reservationRepo.save(reservation);
    }

    @Transactional
    public void cancelReservation(String orderId) {
        Reservation reservation = reservationRepo.getByOrderId(orderId);
        if (reservation == null) {
            return;
        }
        Map<String, Integer> reservedProductMap = reservation.getProducts().stream()
            .collect(Collectors.toMap(Product::getId, Product::getCount));

        productRepo.findAllById(reservedProductMap.keySet()).forEach(product -> {
            product.setCount(product.getCount() + reservedProductMap.get(product.getId()));
            productRepo.save(product);
            saveProductEvent(ProductEventType.UPDATE, product);
        });

        reservationRepo.delete(reservation);
    }

    private void saveProductEvent(ProductEventType eventType, Product product) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setType(eventType);
        productEvent.setProduct(product);

        Event event = new Event();
        event.setPayload(productEvent);
        eventRepo.save(event);
    }

    private void saveReservationEvent(ReservationEventType eventType, Reservation reservation) {
        ReservationEvent productEvent = new ReservationEvent();
        productEvent.setType(eventType);
        productEvent.setReservation(reservation);

        Event event = new Event();
        event.setPayload(productEvent);
        eventRepo.save(event);
    }
}
