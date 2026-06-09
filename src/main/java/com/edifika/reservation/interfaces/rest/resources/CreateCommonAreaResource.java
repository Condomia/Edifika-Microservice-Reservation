package com.edifika.reservation.interfaces.rest.resources;

import com.edifika.reservation.domain.model.valueobjects.CommonAreaType;
import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommonAreaResource(
        @NotBlank String name,
        @NotNull Integer maxCapacity,
        @NotNull EBookingType bookingType,
        @NotNull CommonAreaType type,
        CommonAreaRuleResource rules
) {
}
