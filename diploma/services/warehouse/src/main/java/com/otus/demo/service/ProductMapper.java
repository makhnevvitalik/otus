package com.otus.demo.service;

import com.otus.demo.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductForm form);
}
