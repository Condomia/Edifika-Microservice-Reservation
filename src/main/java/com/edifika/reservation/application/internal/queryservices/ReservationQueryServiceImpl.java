package com.edifika.reservation.application.internal.queryservices;

import com.edifika.reservation.domain.model.aggregates.Reservation;
import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.ECommonAreaStatus;
import com.edifika.reservation.domain.model.valueobjects.EReservationStatus;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReservationQueryServiceImpl {

    private final ReservationRepository reservationRepository;
    private final CommonAreaRepository commonAreaRepository;

    @Transactional(readOnly = true)
    public Map<Integer, Boolean> getAvailability(Long commonAreaId, LocalDate date) {
        // US16: Verify common area status
        CommonArea commonArea = commonAreaRepository.findById(commonAreaId)
                .orElseThrow(() -> new IllegalArgumentException("Common Area not found with id: " + commonAreaId));

        if (commonArea.getStatus() == ECommonAreaStatus.MAINTENANCE) {
            throw new IllegalStateException("Common Area is currently under maintenance.");
        }

        // Get all active reservations for the given area and date
        List<Integer> bookedSlots = reservationRepository
                .findByCommonAreaIdAndReservationDateAndStatus(commonAreaId, date, EReservationStatus.ACTIVE)
                .stream()
                .map(Reservation::getTimeSlot)
                .toList();

        // US16: Build availability list for all 24 hour-slots of the day
        return IntStream.range(0, 24).boxed()
                .collect(Collectors.toMap(
                        hour -> hour,
                        hour -> !bookedSlots.contains(hour) // true if available, false if booked
                ));
    }
}

