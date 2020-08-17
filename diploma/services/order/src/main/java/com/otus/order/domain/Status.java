package com.otus.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    NEW,
    RESERVED,
    PAID,
    DELIVERY,
    COMPLETED,
    CANCELED
}
