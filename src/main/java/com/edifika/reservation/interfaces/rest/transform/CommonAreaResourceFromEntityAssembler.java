package com.edifika.reservation.interfaces.rest.transform;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.interfaces.rest.resources.CommonAreaResource;
import com.edifika.reservation.interfaces.rest.resources.CommonAreaRuleResource;

public class CommonAreaResourceFromEntityAssembler {

    public static CommonAreaResource toResourceFromEntity(CommonArea entity) {
        if (entity == null) return null;

        var r = entity.getRule();

        CommonAreaRuleResource rulesResource = null;
        if (r != null) {
            rulesResource = new CommonAreaRuleResource(
                    r.getMaxReservationHours(),
                    r.getRequiresPayment(),
                    r.getPrice(),
                    r.getRequiresGuarantee(),
                    r.getGuaranteeAmount(),
                    r.getAllowCancellation(),
                    r.getPenaltyHoursBefore(),
                    r.getPenaltyAmount(),
                    r.getRequiresApproval()
            );
        }

        return new CommonAreaResource(
                entity.getId(),
                entity.getName(),
                entity.getType() != null ? entity.getType().name() : null,
                entity.getStatus() != null ? entity.getStatus().name() : null,
                entity.getMaxCapacity(),
                entity.getBookingType() != null ? entity.getBookingType().name() : null,
                rulesResource
        );
    }
}