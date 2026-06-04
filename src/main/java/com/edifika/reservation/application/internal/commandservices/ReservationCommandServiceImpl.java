package com.edifika.reservation.application.internal.commandservices;

import com.edifika.reservation.domain.model.aggregates.Reservation;
import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.EReservationStatus;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationCommandServiceImpl {

    private final ReservationRepository reservationRepository;
    private final CommonAreaRepository commonAreaRepository;

    @Transactional
    public Reservation handleCreateReservation(Long residentId, Long commonAreaId, LocalDate reservationDate, Integer timeSlot) {
        // US17 Bug Fix: Verify time slot is available only checking for ACTIVE reservations
        reservationRepository.findByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(commonAreaId, reservationDate, timeSlot, EReservationStatus.ACTIVE)
                .ifPresent(r -> {
                    throw new IllegalStateException("Time slot is already booked for this common area and date.");
                });

        // US17 Correction: Validate resident has not booked more than 4 ACTIVE hours on the same day
        List<Reservation> residentActiveReservationsToday = reservationRepository.findByResidentIdAndReservationDateAndStatus(residentId, reservationDate, EReservationStatus.ACTIVE);
        if (residentActiveReservationsToday.size() >= 4) {
            throw new IllegalStateException("Resident cannot book more than 4 active hours on the same day.");
        }

        CommonArea commonArea = commonAreaRepository.findById(commonAreaId)
                .orElseThrow(() -> new IllegalArgumentException("Common Area not found with id: " + commonAreaId));

        // Create and save the reservation
        Reservation reservation = new Reservation(residentId, commonArea, reservationDate, timeSlot);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation handleCancelReservation(Long reservationId) {
        // US20: Find reservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + reservationId));

        // US20 Correction: Cancel and apply penalty if necessary, passing current time
        reservation.cancel(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }
}

