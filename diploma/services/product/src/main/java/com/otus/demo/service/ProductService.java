package com.otus.demo.service;

import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import com.otus.demo.domain.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private static final QueryConversionPipeline PIPELINE = QueryConversionPipeline.defaultPipeline();
    private static final MongoVisitor VISITOR = new MongoVisitor();

    private final MongoTemplate template;

    @Cacheable("product_get")
    public Product getCached(String id) {
        log.info("Get from service");
        return template.findOne(Query.query(Criteria.where("_id").is(id)), Product.class);
    }

    public Product get(String id) {
        return template.findOne(Query.query(Criteria.where("_id").is(id)), Product.class);
    }

    public Page<Product> search(String rsql, Pageable pageable) {
        log.info("Search from service");
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

    @CacheEvict(value = "product_get", key = "#product.id")
    public void save(Product product) {
        template.save(product);
    }

    @CacheEvict("product_get")
    public void delete(String id) {
        template.remove(Query.query(Criteria.where("_id").is(id)), Product.class);
    }
}
