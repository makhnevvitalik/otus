package com.otus.demo.controller;

import com.otus.demo.dto.PageDto;
import com.otus.demo.dto.ProductDto;
import com.otus.demo.dto.ProductDtoMapper;
import com.otus.demo.dto.ProductFormDto;
import com.otus.demo.service.ProductService;
import io.micrometer.core.annotation.Timed;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/warehouse/products")
@Validated
@Timed("warehouse.product.requests")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductDtoMapper mapper;

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable String id) {
        return mapper.toDto(productService.get(id));
    }

    @GetMapping
    public List<ProductDto> getBatch(@RequestParam List<String> id) {
        return productService.get(id).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public PageDto<ProductDto> search(@Valid @RequestParam(required = false) String query, Pageable pageable) {
        return mapper.toDto(productService.search(query, pageable).map(mapper::toDto));
    }

    @PostMapping
    public ProductDto add(@Valid @RequestBody ProductFormDto productForm) {
        return mapper.toDto(productService.add(mapper.fromDto(productForm)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Valid @RequestBody ProductFormDto productForm) {
        productService.update(id, mapper.fromDto(productForm));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }
}
