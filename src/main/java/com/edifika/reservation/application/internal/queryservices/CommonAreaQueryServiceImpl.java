package com.edifika.reservation.application.internal.queryservices;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.services.CommonAreaQueryService;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommonAreaQueryServiceImpl implements CommonAreaQueryService {

    private final CommonAreaRepository commonAreaRepository;

    public CommonAreaQueryServiceImpl(CommonAreaRepository commonAreaRepository) {
        this.commonAreaRepository = commonAreaRepository;
    }

    @Override
    public List<CommonArea> handleGetAllCommonAreas() {
        return commonAreaRepository.findAll();
    }
}