package com.edifika.reservation.interfaces.rest.resources;

import java.time.LocalDate;

public record ReservationResource(
        Long id,
        Long residentId,
        Long commonAreaId,
        LocalDate reservationDate,
        Integer timeSlot,
        String status,
        String qrCodeAccess,
        boolean penaltyApplied
) {
}

