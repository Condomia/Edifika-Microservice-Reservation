package com.edifika.reservation.domain.model.entities;

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
    @Positive
    @Column(nullable = false)
    private Integer maxCapacity;

    public CommonArea(String name, Integer maxCapacity) {
        this.name = name;
        this.status = ECommonAreaStatus.AVAILABLE;
        this.maxCapacity = maxCapacity;
    }

    public void putInMaintenance() {
        this.status = ECommonAreaStatus.MAINTENANCE;
    }

    public void makeAvailable() {
        this.status = ECommonAreaStatus.AVAILABLE;
    }
}
