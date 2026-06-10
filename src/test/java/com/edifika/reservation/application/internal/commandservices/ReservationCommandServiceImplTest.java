package com.edifika.reservation.application.internal.commandservices;

import com.edifika.reservation.domain.model.aggregates.Reservation;
import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import com.edifika.reservation.domain.model.valueobjects.EReservationStatus;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationCommandServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CommonAreaRepository commonAreaRepository;

    @InjectMocks
    private ReservationCommandServiceImpl reservationCommandService;

    @Test
    void handleCreateReservationSuccessfullyForExclusiveArea() {
        var residentId = 1L;
        var commonAreaId = 10L;
        var reservationDate = LocalDate.now();
        var timeSlot = 9;

        var commonArea = mock(CommonArea.class);
        var savedReservation = mock(Reservation.class);

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.of(commonArea));

        when(reservationRepository.findByResidentIdAndReservationDateAndStatus(
                residentId, reservationDate, EReservationStatus.ACTIVE))
                .thenReturn(List.of());

        when(commonArea.getBookingType())
                .thenReturn(EBookingType.EXCLUSIVE);

        when(reservationRepository.findByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                commonAreaId, reservationDate, timeSlot, EReservationStatus.ACTIVE))
                .thenReturn(Optional.empty());

        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(savedReservation);

        var result = reservationCommandService.handleCreateReservation(
                residentId,
                commonAreaId,
                reservationDate,
                timeSlot
        );

        assertNotNull(result);
        assertEquals(savedReservation, result);

        verify(commonAreaRepository, times(1)).findById(commonAreaId);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void handleCreateReservationThrowsExceptionWhenCommonAreaNotFound() {
        var residentId = 1L;
        var commonAreaId = 99L;
        var reservationDate = LocalDate.now();
        var timeSlot = 9;

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> reservationCommandService.handleCreateReservation(
                        residentId,
                        commonAreaId,
                        reservationDate,
                        timeSlot
                ));

        assertEquals("Common Area not found with id: 99", exception.getMessage());

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void handleCreateReservationThrowsExceptionWhenResidentHasFourActiveReservations() {
        var residentId = 1L;
        var commonAreaId = 10L;
        var reservationDate = LocalDate.now();
        var timeSlot = 9;

        var commonArea = mock(CommonArea.class);

        var reservation1 = mock(Reservation.class);
        var reservation2 = mock(Reservation.class);
        var reservation3 = mock(Reservation.class);
        var reservation4 = mock(Reservation.class);

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.of(commonArea));

        when(reservationRepository.findByResidentIdAndReservationDateAndStatus(
                residentId, reservationDate, EReservationStatus.ACTIVE))
                .thenReturn(List.of(reservation1, reservation2, reservation3, reservation4));

        var exception = assertThrows(IllegalStateException.class,
                () -> reservationCommandService.handleCreateReservation(
                        residentId,
                        commonAreaId,
                        reservationDate,
                        timeSlot
                ));

        assertEquals("Resident cannot book more than 4 active hours on the same day.", exception.getMessage());

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void handleCreateReservationThrowsExceptionWhenExclusiveTimeSlotAlreadyBooked() {
        var residentId = 1L;
        var commonAreaId = 10L;
        var reservationDate = LocalDate.now();
        var timeSlot = 9;

        var commonArea = mock(CommonArea.class);
        var existingReservation = mock(Reservation.class);

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.of(commonArea));

        when(reservationRepository.findByResidentIdAndReservationDateAndStatus(
                residentId, reservationDate, EReservationStatus.ACTIVE))
                .thenReturn(List.of());

        when(commonArea.getBookingType())
                .thenReturn(EBookingType.EXCLUSIVE);

        when(reservationRepository.findByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                commonAreaId, reservationDate, timeSlot, EReservationStatus.ACTIVE))
                .thenReturn(Optional.of(existingReservation));

        var exception = assertThrows(IllegalStateException.class,
                () -> reservationCommandService.handleCreateReservation(
                        residentId,
                        commonAreaId,
                        reservationDate,
                        timeSlot
                ));

        assertEquals("Time slot is already booked for this exclusive area.", exception.getMessage());

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void handleCreateReservationSuccessfullyForSharedArea() {
        var residentId = 1L;
        var commonAreaId = 10L;
        var reservationDate = LocalDate.now();
        var timeSlot = 9;

        var commonArea = mock(CommonArea.class);
        var savedReservation = mock(Reservation.class);

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.of(commonArea));

        when(reservationRepository.findByResidentIdAndReservationDateAndStatus(
                residentId, reservationDate, EReservationStatus.ACTIVE))
                .thenReturn(List.of());

        when(commonArea.getBookingType())
                .thenReturn(EBookingType.SHARED);

        when(reservationRepository.countByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                commonAreaId, reservationDate, timeSlot, EReservationStatus.ACTIVE))
                .thenReturn(2L);

        when(commonArea.getMaxCapacity())
                .thenReturn(5);

        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(savedReservation);

        var result = reservationCommandService.handleCreateReservation(
                residentId,
                commonAreaId,
                reservationDate,
                timeSlot
        );

        assertNotNull(result);
        assertEquals(savedReservation, result);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void handleCreateReservationThrowsExceptionWhenSharedAreaCapacityIsFull() {
        var residentId = 1L;
        var commonAreaId = 10L;
        var reservationDate = LocalDate.now();
        var timeSlot = 9;

        var commonArea = mock(CommonArea.class);

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.of(commonArea));

        when(reservationRepository.findByResidentIdAndReservationDateAndStatus(
                residentId, reservationDate, EReservationStatus.ACTIVE))
                .thenReturn(List.of());

        when(commonArea.getBookingType())
                .thenReturn(EBookingType.SHARED);

        when(reservationRepository.countByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                commonAreaId, reservationDate, timeSlot, EReservationStatus.ACTIVE))
                .thenReturn(5L);

        when(commonArea.getMaxCapacity())
                .thenReturn(5);

        var exception = assertThrows(IllegalStateException.class,
                () -> reservationCommandService.handleCreateReservation(
                        residentId,
                        commonAreaId,
                        reservationDate,
                        timeSlot
                ));

        assertEquals("The shared area has reached its maximum capacity for this time slot.", exception.getMessage());

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void handleCancelReservationSuccessfully() {
        var reservationId = 1L;
        var reservation = mock(Reservation.class);

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.save(reservation))
                .thenReturn(reservation);

        var result = reservationCommandService.handleCancelReservation(reservationId);

        assertNotNull(result);
        assertEquals(reservation, result);

        verify(reservation, times(1))
                .cancel(any());

        verify(reservationRepository, times(1))
                .save(reservation);
    }

    @Test
    void handleCancelReservationThrowsExceptionWhenReservationNotFound() {
        var reservationId = 99L;

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> reservationCommandService.handleCancelReservation(reservationId));

        assertEquals("Reservation not found with id: 99", exception.getMessage());

        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}