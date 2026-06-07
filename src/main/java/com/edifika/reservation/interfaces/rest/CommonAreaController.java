package com.edifika.reservation.interfaces.rest;

import com.edifika.reservation.application.internal.commandservices.CommonAreaCommandServiceImpl;
import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.interfaces.rest.resources.CommonAreaResource;
import com.edifika.reservation.interfaces.rest.resources.CreateCommonAreaResource;
import com.edifika.reservation.interfaces.rest.transform.CommonAreaResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/common-areas")
@RequiredArgsConstructor
@Validated
@Tag(name = "Common Areas")
public class CommonAreaController {

    private final CommonAreaCommandServiceImpl commonAreaCommandService;

    @PostMapping
    public ResponseEntity<CommonAreaResource> createCommonArea(@RequestBody @Validated CreateCommonAreaResource resource) {
        CommonArea newCommonArea = commonAreaCommandService.handleCreateCommonArea(
                resource.name(),
                resource.maxCapacity(),
                resource.bookingType()
        );
        CommonAreaResource commonAreaResource = CommonAreaResourceFromEntityAssembler.toResourceFromEntity(newCommonArea);
        return new ResponseEntity<>(commonAreaResource, HttpStatus.CREATED);
    }
}
