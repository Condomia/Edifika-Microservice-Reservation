package com.edifika.reservation.domain.services;

import com.edifika.reservation.domain.model.entities.CommonArea;
import java.util.List;

public interface CommonAreaQueryService {
    List<CommonArea> handleGetAllCommonAreas();
}