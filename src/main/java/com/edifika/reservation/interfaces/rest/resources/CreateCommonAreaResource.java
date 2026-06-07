package com.edifika.reservation.interfaces.rest.resources;

import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommonAreaResource(
        @NotBlank(message = "Name is mandatory")
        String name,

        @Min(value = 1, message = "Max capacity must be at least 1")
        Integer maxCapacity,

        @NotNull(message = "Booking type is mandatory")
        EBookingType bookingType
) {
}
