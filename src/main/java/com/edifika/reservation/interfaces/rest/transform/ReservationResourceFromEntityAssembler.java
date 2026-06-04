package com.edifika.reservation.interfaces.rest.transform;

import com.edifika.reservation.domain.model.aggregates.Reservation;
import com.edifika.reservation.interfaces.rest.resources.ReservationResource;

public class ReservationResourceFromEntityAssembler {
    public static ReservationResource toResourceFromEntity(Reservation entity) {
        return new ReservationResource(
                entity.getId(),
                entity.getResidentId(),
                entity.getCommonArea().getId(),
                entity.getReservationDate(),
                entity.getTimeSlot(),
                entity.getStatus().name(),
                entity.getQrCodeAccess(),
                entity.isPenaltyApplied()
        );
    }
}

