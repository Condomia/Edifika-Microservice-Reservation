package com.edifika.reservation.domain.model.entities;

import com.edifika.reservation.domain.model.valueobjects.CommonAreaType;
import com.edifika.reservation.domain.model.valueobjects.EBookingType;
import com.edifika.reservation.domain.model.valueobjects.ECommonAreaStatus;
import com.edifika.shared.domain.model.entity.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CommonArea extends AuditableModel {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECommonAreaStatus status;

    @NotNull
    @Column(nullable = false)
    private Integer maxCapacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EBookingType bookingType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonAreaType type;

    @OneToOne(mappedBy = "commonArea", cascade = CascadeType.ALL, orphanRemoval = true)
    private CommonAreaRule rule;


    public CommonArea(String name, Integer maxCapacity, EBookingType bookingType, CommonAreaType type) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.bookingType = bookingType;
        this.type = type;
        this.status = ECommonAreaStatus.AVAILABLE;
    }

    public void setRule(CommonAreaRule rule) {
        this.rule = rule;
    }

    public void putInMaintenance() {
        this.status = ECommonAreaStatus.MAINTENANCE;
    }

    public void makeAvailable() {
        this.status = ECommonAreaStatus.AVAILABLE;
    }
}
