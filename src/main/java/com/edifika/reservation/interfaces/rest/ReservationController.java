package com.edifika.reservation.interfaces.rest;

import com.edifika.reservation.application.internal.commandservices.ReservationCommandServiceImpl;
import com.edifika.reservation.application.internal.queryservices.ReservationQueryServiceImpl;
import com.edifika.reservation.domain.model.aggregates.Reservation;
import com.edifika.reservation.interfaces.rest.resources.CreateReservationResource;
import com.edifika.reservation.interfaces.rest.resources.ReservationResource;
import com.edifika.reservation.interfaces.rest.transform.ReservationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Validated
@Tag(name = "Reservations")
public class ReservationController {

    private final ReservationCommandServiceImpl reservationCommandService;
    private final ReservationQueryServiceImpl reservationQueryService;

    @PostMapping
    public ResponseEntity<ReservationResource> createReservation(@RequestBody @Validated CreateReservationResource resource) {
        Reservation newReservation = reservationCommandService.handleCreateReservation(
                resource.residentId(),
                resource.commonAreaId(),
                resource.reservationDate(),
                resource.timeSlot()
        );
        ReservationResource reservationResource = ReservationResourceFromEntityAssembler.toResourceFromEntity(newReservation);
        return new ResponseEntity<>(reservationResource, HttpStatus.CREATED);
    }

    @PostMapping("/{reservationId}/cancelations")
    public ResponseEntity<ReservationResource> cancelReservation(@PathVariable Long reservationId) {
        Reservation updatedReservation = reservationCommandService.handleCancelReservation(reservationId);
        ReservationResource reservationResource = ReservationResourceFromEntityAssembler.toResourceFromEntity(updatedReservation);
        return ResponseEntity.ok(reservationResource);
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<Integer, Integer>> getCommonAreaAvailability(
            @RequestParam Long commonAreaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Map<Integer, Integer> availability = reservationQueryService.getAvailability(commonAreaId, date);
        return ResponseEntity.ok(availability);
    }
}
