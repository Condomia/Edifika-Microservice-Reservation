package com.edifika.reservation.interfaces.rest;

import com.edifika.reservation.application.internal.commandservices.ReservationCommandServiceImpl;
import com.edifika.reservation.application.internal.queryservices.ReservationQueryServiceImpl;
import com.edifika.reservation.domain.model.aggregates.Reservation;
import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.EReservationStatus;
import com.edifika.reservation.interfaces.rest.resources.CreateReservationResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationCommandServiceImpl reservationCommandService;

    @Mock
    private ReservationQueryServiceImpl reservationQueryService;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    void createReservation() {
        var date = LocalDate.now();

        var resource = new CreateReservationResource(
                1L,
                10L,
                date,
                9
        );

        var commonArea = mock(CommonArea.class);
        var reservation = mock(Reservation.class);

        when(commonArea.getId()).thenReturn(10L);

        when(reservation.getId()).thenReturn(100L);
        when(reservation.getResidentId()).thenReturn(1L);
        when(reservation.getCommonArea()).thenReturn(commonArea);
        when(reservation.getReservationDate()).thenReturn(date);
        when(reservation.getTimeSlot()).thenReturn(9);
        when(reservation.getStatus()).thenReturn(EReservationStatus.ACTIVE);

        when(reservationCommandService.handleCreateReservation(1L, 10L, date, 9))
                .thenReturn(reservation);

        var response = reservationController.createReservation(resource);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(100L, response.getBody().id());
        assertEquals(1L, response.getBody().residentId());

        verify(reservationCommandService, times(1))
                .handleCreateReservation(1L, 10L, date, 9);
    }

    @Test
    void cancelReservation() {
        var date = LocalDate.now();

        var commonArea = mock(CommonArea.class);
        var reservation = mock(Reservation.class);

        when(commonArea.getId()).thenReturn(10L);

        when(reservation.getId()).thenReturn(100L);
        when(reservation.getResidentId()).thenReturn(1L);
        when(reservation.getCommonArea()).thenReturn(commonArea);
        when(reservation.getReservationDate()).thenReturn(date);
        when(reservation.getTimeSlot()).thenReturn(9);
        when(reservation.getStatus()).thenReturn(EReservationStatus.CANCELLED);

        when(reservationCommandService.handleCancelReservation(100L))
                .thenReturn(reservation);

        var response = reservationController.cancelReservation(100L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(100L, response.getBody().id());
        assertEquals("CANCELLED", response.getBody().status());
        verify(reservationCommandService, times(1))
                .handleCancelReservation(100L);
    }

    @Test
    void getCommonAreaAvailability() {
        var date = LocalDate.now();

        var availability = Map.of(
                9, 1,
                10, 0
        );

        when(reservationQueryService.getAvailability(10L, date))
                .thenReturn(availability);

        var response = reservationController.getCommonAreaAvailability(10L, date);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(availability, response.getBody());

        verify(reservationQueryService, times(1))
                .getAvailability(10L, date);
    }
}