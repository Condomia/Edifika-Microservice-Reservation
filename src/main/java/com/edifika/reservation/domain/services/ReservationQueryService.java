package com.edifika.reservation.domain.services;

import com.edifika.reservation.domain.model.aggregates.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReservationQueryService {
    Map<Integer, Integer> getAvailability(Long commonAreaId, LocalDate date);
    List<Reservation> handleGetAllReservations();
}