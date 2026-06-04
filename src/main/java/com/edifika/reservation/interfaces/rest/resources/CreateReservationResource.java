package com.edifika.reservation.interfaces.rest.resources;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateReservationResource(
        @NotNull Long residentId,
        @NotNull Long commonAreaId,
        @NotNull @FutureOrPresent LocalDate reservationDate,
        @NotNull @Min(0) @Max(23) Integer timeSlot
) {
}

