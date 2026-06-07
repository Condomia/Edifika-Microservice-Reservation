package com.edifika.reservation.interfaces.rest.transform;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.interfaces.rest.resources.CommonAreaResource;

public class CommonAreaResourceFromEntityAssembler {

    public static CommonAreaResource toResourceFromEntity(CommonArea entity) {
        return new CommonAreaResource(
                entity.getId(),
                entity.getName(),
                entity.getStatus().name(),
                entity.getMaxCapacity()
        );
    }
}

