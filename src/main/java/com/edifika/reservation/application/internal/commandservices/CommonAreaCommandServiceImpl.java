package com.edifika.reservation.application.internal.commandservices;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.entities.CommonAreaRule;
import com.edifika.reservation.domain.services.CommonAreaCommandService;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import com.edifika.reservation.interfaces.rest.resources.CommonAreaRuleResource;
import com.edifika.reservation.interfaces.rest.resources.CreateCommonAreaResource;
import org.springframework.stereotype.Service;

@Service
public class CommonAreaCommandServiceImpl implements CommonAreaCommandService {

    private final CommonAreaRepository commonAreaRepository;

    public CommonAreaCommandServiceImpl(CommonAreaRepository commonAreaRepository) {
        this.commonAreaRepository = commonAreaRepository;
    }

    @Override
    public CommonArea handleCreateCommonArea(CreateCommonAreaResource resource) {
        var commonArea = new CommonArea(
                resource.name(),
                resource.maxCapacity(),
                resource.bookingType(),
                resource.type()
        );

        if (resource.rules() != null) {
            CommonAreaRuleResource ruleResource = resource.rules();
            var rule = new CommonAreaRule(
                    ruleResource.maxReservationHours(),
                    ruleResource.requiresPayment(),
                    ruleResource.price(),
                    ruleResource.requiresGuarantee(),
                    ruleResource.guaranteeAmount(),
                    ruleResource.allowCancellation(),
                    ruleResource.penaltyHoursBefore(),
                    ruleResource.penaltyAmount(),
                    ruleResource.requiresApproval(),
                    null // CommonArea is set below
            );
            // Establish bidirectional relationship
            rule.setCommonArea(commonArea);
            commonArea.setRule(rule);
        }

        return commonAreaRepository.save(commonArea);
    }
}
