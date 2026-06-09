package com.edifika.reservation.interfaces.rest.resources;

import java.math.BigDecimal;

public record CommonAreaRuleResource(
    Integer maxReservationHours,
    Boolean requiresPayment,
    BigDecimal price,
    Boolean requiresGuarantee,
    BigDecimal guaranteeAmount,
    Boolean allowCancellation,
    Integer penaltyHoursBefore,
    BigDecimal penaltyAmount,
    Boolean requiresApproval
) {
}

