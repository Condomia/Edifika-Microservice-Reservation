package com.edifika.reservation.application.internal.queryservices;

import com.edifika.reservation.domain.model.aggregates.Reservation;
import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import com.edifika.reservation.domain.model.valueobjects.ECommonAreaStatus;
import com.edifika.reservation.domain.model.valueobjects.EReservationStatus;
import com.edifika.reservation.domain.services.ReservationQueryService;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;
    private final CommonAreaRepository commonAreaRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Integer> getAvailability(Long commonAreaId, LocalDate date) {
        CommonArea commonArea = commonAreaRepository.findById(commonAreaId)
                .orElseThrow(() -> new IllegalArgumentException("Common Area not found with id: " + commonAreaId));

        if (commonArea.getStatus() == ECommonAreaStatus.MAINTENANCE) {
            throw new IllegalStateException("Common Area is currently under maintenance.");
        }

        Map<Integer, Integer> availabilityMap = new LinkedHashMap<>();
        int totalHours = 24;

        for (int hour = 0; hour < totalHours; hour++) {
            if (commonArea.getBookingType() == EBookingType.EXCLUSIVE) {
                boolean isBooked = reservationRepository.findByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                        commonAreaId, date, hour, EReservationStatus.ACTIVE).isPresent();
                availabilityMap.put(hour, isBooked ? 0 : 1);
            } else if (commonArea.getBookingType() == EBookingType.SHARED) {
                long activeReservations = reservationRepository.countByCommonAreaIdAndReservationDateAndTimeSlotAndStatus(
                        commonAreaId, date, hour, EReservationStatus.ACTIVE);
                int availableSlots = commonArea.getMaxCapacity() - (int) activeReservations;
                availabilityMap.put(hour, Math.max(0, availableSlots));
            }
        }
        return availabilityMap;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> handleGetAllReservations() {
        return reservationRepository.findAll();
    }
}