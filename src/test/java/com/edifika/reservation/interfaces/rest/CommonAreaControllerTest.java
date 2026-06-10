package com.edifika.reservation.interfaces.rest;

import com.edifika.reservation.application.internal.commandservices.CommonAreaCommandServiceImpl;
import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.CommonAreaType;
import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import com.edifika.reservation.interfaces.rest.resources.CreateCommonAreaResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonAreaControllerTest {

    @Mock
    private CommonAreaCommandServiceImpl commonAreaCommandService;

    @InjectMocks
    private CommonAreaController commonAreaController;

    @Test
    void createCommonAreaSuccessfully() {
        var resource = new CreateCommonAreaResource(
                "Piscina",
                20,
                EBookingType.EXCLUSIVE,
                CommonAreaType.PARKING_AREA,
                null
        );

        var commonArea = mock(CommonArea.class);

        when(commonArea.getId()).thenReturn(1L);
        when(commonArea.getName()).thenReturn("Piscina");
        when(commonArea.getMaxCapacity()).thenReturn(20);
        when(commonArea.getBookingType()).thenReturn(EBookingType.EXCLUSIVE);
        when(commonArea.getType()).thenReturn(CommonAreaType.PARKING_AREA);

        when(commonAreaCommandService.handleCreateCommonArea(resource))
                .thenReturn(commonArea);

        var response = commonAreaController.createCommonArea(resource);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(1L, response.getBody().id());
        assertEquals("Piscina", response.getBody().name());
        assertEquals(20, response.getBody().maxCapacity());

        verify(commonAreaCommandService, times(1))
                .handleCreateCommonArea(resource);
    }
}