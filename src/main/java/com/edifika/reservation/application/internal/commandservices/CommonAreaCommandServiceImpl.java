package com.edifika.reservation.application.internal.commandservices;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonAreaCommandServiceImpl {

    private final CommonAreaRepository commonAreaRepository;

    public CommonArea handleCreateCommonArea(String name, Integer maxCapacity, EBookingType bookingType) {
        CommonArea newCommonArea = new CommonArea(name, maxCapacity, bookingType);
        return commonAreaRepository.save(newCommonArea);
    }
}
