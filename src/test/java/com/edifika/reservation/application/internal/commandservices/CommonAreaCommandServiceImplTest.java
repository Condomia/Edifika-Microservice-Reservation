package com.edifika.reservation.application.internal.commandservices;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.CommonAreaType;
import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import com.edifika.reservation.infrastructure.persistence.jpa.repositories.CommonAreaRepository;
import com.edifika.reservation.interfaces.rest.resources.CommonAreaRuleResource;
import com.edifika.reservation.interfaces.rest.resources.CreateCommonAreaResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonAreaCommandServiceImplTest {

    @Mock
    private CommonAreaRepository commonAreaRepository;

    @InjectMocks
    private CommonAreaCommandServiceImpl commonAreaCommandService;

    @Test
    void handleCreateCommonAreaSuccessfullyWithoutRules() {
        var resource = new CreateCommonAreaResource(
                "Piscina",
                20,
                EBookingType.SHARED,
                CommonAreaType.BBQ_AREA,
                null
        );

        var savedCommonArea = mock(CommonArea.class);

        when(commonAreaRepository.save(any(CommonArea.class)))
                .thenReturn(savedCommonArea);

        var result = commonAreaCommandService.handleCreateCommonArea(resource);

        assertNotNull(result);
        assertEquals(savedCommonArea, result);

        verify(commonAreaRepository, times(1))
                .save(any(CommonArea.class));
    }

    @Test
    void handleCreateCommonAreaSuccessfullyWithRules() {
        var ruleResource = new CommonAreaRuleResource(
                2,
                true,
                BigDecimal.valueOf(50.0),
                true,
                BigDecimal.valueOf(100.0),
                true,
                24,
                BigDecimal.valueOf(25.0),
                true
        );

        var resource = new CreateCommonAreaResource(
                "Salón de eventos",
                50,
                EBookingType.SHARED,
                CommonAreaType.BBQ_AREA,
                ruleResource
        );

        var savedCommonArea = mock(CommonArea.class);

        when(commonAreaRepository.save(any(CommonArea.class)))
                .thenReturn(savedCommonArea);

        var result = commonAreaCommandService.handleCreateCommonArea(resource);

        assertNotNull(result);
        assertEquals(savedCommonArea, result);

        verify(commonAreaRepository, times(1))
                .save(any(CommonArea.class));
    }
}