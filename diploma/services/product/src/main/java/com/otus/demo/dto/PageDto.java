package com.otus.demo.dto;

import java.util.List;
import lombok.Data;

@Data
public class PageDto<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int size;
    private boolean empty;
}
