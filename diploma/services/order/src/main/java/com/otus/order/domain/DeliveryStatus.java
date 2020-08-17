package com.otus.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

    CONFIRMED,
    SENT,
    DELIVERED
}
