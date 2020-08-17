package com.otus.demo.integration;

import com.otus.demo.dto.ReservationDto;
import com.otus.demo.service.ReservationEvent.ReservationEventType;
import lombok.Data;

@Data
public class ReservationEventDto {

    private String id;
    private ReservationEventType type;
    private ReservationDto reservation;
}
