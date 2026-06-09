package com.edifika.reservation.domain.model.aggregates;

import com.edifika.reservation.domain.model.entities.CommonArea;
import com.edifika.reservation.domain.model.valueobjects.EReservationStatus;
import com.edifika.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Reservation extends AuditableAbstractAggregateRoot<Reservation> {

    @NotNull
    private Long residentId;

    @ManyToOne
    @JoinColumn(name = "common_area_id", nullable = false)
    private CommonArea commonArea;

    @NotNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDate reservationDate;

    @NotNull
    @Column(nullable = false)
    private Integer timeSlot; // Represents the hour block (e.g., 9 for 9:00-10:00)

    private Integer numberOfGuests;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EReservationStatus status;

    @Column(unique = true)
    private String qrCodeAccess;

    private boolean penaltyApplied;

    public Reservation(Long residentId, CommonArea commonArea, LocalDate reservationDate, Integer timeSlot) {
        this.residentId = residentId;
        this.commonArea = commonArea;
        this.reservationDate = reservationDate;
        this.timeSlot = timeSlot;
        this.status = EReservationStatus.ACTIVE;
        this.qrCodeAccess = UUID.randomUUID().toString();
        this.penaltyApplied = false;
    }

    public void cancel(LocalDateTime cancellationTime) {
        this.status = EReservationStatus.CANCELLED;
        if (isCancellationLate(cancellationTime)) {
            this.penaltyApplied = true;
        }
    }

    private boolean isCancellationLate(LocalDateTime cancellationTime) {
        LocalDateTime reservationStartDateTime = LocalDateTime.of(this.reservationDate, LocalTime.of(this.timeSlot, 0));
        return cancellationTime.isAfter(reservationStartDateTime.minusHours(24));
    }
}
