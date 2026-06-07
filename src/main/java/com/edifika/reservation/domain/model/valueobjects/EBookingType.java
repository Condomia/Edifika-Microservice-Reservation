package com.edifika.reservation.domain.model.valueobjects;

/**
 * Represents the booking type for a common area.
 * EXCLUSIVE: Only one reservation is allowed per time slot.
 * SHARED: Multiple reservations are allowed up to the area's maximum capacity.
 */
public enum EBookingType {
    EXCLUSIVE,
    SHARED
}

