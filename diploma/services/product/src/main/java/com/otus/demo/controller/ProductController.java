package com.otus.demo.controller;

import com.otus.demo.dto.PageDto;
import com.otus.demo.dto.ProductDto;
import com.otus.demo.dto.ProductMapper;
import com.otus.demo.service.ProductService;
import io.micrometer.core.annotation.Timed;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@Validated
@Timed("product.requests")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService searchService;
    private final ProductMapper mapper;

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable String id) {
        return mapper.toDto(searchService.getCached(id));
    }

    @GetMapping("/search")
    public PageDto<ProductDto> search(@Valid @RequestParam(required = false) String query, Pageable pageable) {
        return mapper.toDto(searchService.search(query, pageable).map(mapper::toDto));
    }
}
