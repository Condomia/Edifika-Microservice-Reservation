package com.edifika.reservation.interfaces.rest.resources;

public record CommonAreaResource(
        Long id,
        String name,
        String status,
        Integer maxCapacity
) {
}

