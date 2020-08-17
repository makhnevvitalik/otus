package com.otus.demo.dto;

import com.otus.demo.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    ProductDto toDto(Product product);

    default <T> PageDto<T> toDto(Page<T> page) {
        PageDto<T> dto = new PageDto<>();
        dto.setContent(page.getContent());
        dto.setEmpty(page.isEmpty());
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        dto.setSize(page.getSize());
        return dto;
    }
}
