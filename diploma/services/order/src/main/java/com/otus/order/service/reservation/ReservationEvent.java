package com.otus.order.service.reservation;

import lombok.Data;

@Data
public class ReservationEvent {

    private String id;
    private ReservationEventType type;
    private Reservation reservation;

    public enum ReservationEventType {
        OK,
        REJECTED
    }
}
