package com.edifika.reservation.infrastructure.persistence.jpa.repositories;

import com.edifika.reservation.domain.model.aggregates.Reservation;
import com.edifika.reservation.domain.model.valueobjects.EReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(Long commonAreaId, LocalDate reservationDate, Integer timeSlot, EReservationStatus status);
    List<Reservation> findByResidentId(Long residentId);
    List<Reservation> findByResidentIdAndReservationDateAndStatus(Long residentId, LocalDate reservationDate, EReservationStatus status);
    List<Reservation> findByCommonAreaIdAndReservationDateAndStatus(Long commonAreaId, LocalDate date, EReservationStatus status);
}
