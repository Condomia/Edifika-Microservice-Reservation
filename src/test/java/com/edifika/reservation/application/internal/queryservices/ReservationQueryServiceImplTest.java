package com.edifika.reservation.application.internal.queryservices;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import com.edifika.reservation.domain.model.valueobjects.ECommonAreaStatus;
import com.edifika.reservation.domain.model.valueobjects.EReservationStatus;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationQueryServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CommonAreaRepository commonAreaRepository;

    @InjectMocks
    private ReservationQueryServiceImpl reservationQueryService;

    @Test
    void getAvailabilitySuccessfullyForExclusiveArea() {
        var commonAreaId = 1L;
        var date = LocalDate.now();
        var commonArea = mock(CommonArea.class);

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.of(commonArea));

        when(commonArea.getStatus())
                .thenReturn(ECommonAreaStatus.AVAILABLE);

        when(commonArea.getBookingType())
                .thenReturn(EBookingType.EXCLUSIVE);

        when(reservationRepository.findByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                anyLong(),
                any(),
                anyInt(),
                any()
        )).thenReturn(Optional.empty());

        var result = reservationQueryService.getAvailability(commonAreaId, date);

        assertNotNull(result);
        assertEquals(24, result.size());
        assertEquals(1, result.get(0));

        verify(commonAreaRepository, times(1))
                .findById(commonAreaId);
    }

    @Test
    void getAvailabilitySuccessfullyForSharedArea() {
        var commonAreaId = 1L;
        var date = LocalDate.now();
        var commonArea = mock(CommonArea.class);

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.of(commonArea));

        when(commonArea.getStatus())
                .thenReturn(ECommonAreaStatus.AVAILABLE);

        when(commonArea.getBookingType())
                .thenReturn(EBookingType.SHARED);

        when(commonArea.getMaxCapacity())
                .thenReturn(5);

        when(reservationRepository.countByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                anyLong(),
                any(),
                anyInt(),
                any()
        )).thenReturn(0L);

        var result = reservationQueryService.getAvailability(commonAreaId, date);

        assertNotNull(result);
        assertEquals(24, result.size());
        assertEquals(5, result.get(0));

        verify(commonAreaRepository, times(1))
                .findById(commonAreaId);
    }

    @Test
    void getAvailabilityThrowsExceptionWhenCommonAreaIsUnderMaintenance() {
        var commonAreaId = 1L;
        var date = LocalDate.now();
        var commonArea = mock(CommonArea.class);

        when(commonAreaRepository.findById(commonAreaId))
                .thenReturn(Optional.of(commonArea));

        when(commonArea.getStatus())
                .thenReturn(ECommonAreaStatus.MAINTENANCE);

        var exception = assertThrows(IllegalStateException.class,
                () -> reservationQueryService.getAvailability(commonAreaId, date));

        assertEquals("Common Area is currently under maintenance.", exception.getMessage());

        verify(reservationRepository, never())
                .findByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                        anyLong(),
                        any(),
                        anyInt(),
                        any()
                );
    }
}