package com.edifika.reservation.domain.services;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.interfaces.rest.resources.CreateCommonAreaResource;

public interface CommonAreaCommandService {
    CommonArea handleCreateCommonArea(CreateCommonAreaResource resource);
}

