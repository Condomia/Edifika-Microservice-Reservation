package com.edifika.reservation.interfaces.rest.resources;

public record CommonAreaResource(
        Long id,
        String name,
        String type,         // <-- Agregado para el Frontend
        String status,
        Integer maxCapacity,
        String bookingType,  // <-- Agregado para el Frontend
        CommonAreaRuleResource rules // <-- Agregado para retornar las reglas anidadas
) {
}