package com.otus.demo.service;

import com.otus.demo.domain.Reservation;
import lombok.Data;

@Data
public class ReservationEvent {

    private ReservationEventType type;
    private Reservation reservation;

    public enum ReservationEventType {
        OK,
        REJECTED
    }
}
