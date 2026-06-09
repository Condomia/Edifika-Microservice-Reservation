package com.edifika.reservation.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class CommonAreaRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer maxReservationHours;
    private Boolean requiresPayment;
    private BigDecimal price;
    private Boolean requiresGuarantee;
    private BigDecimal guaranteeAmount;
    private Boolean allowCancellation;
    private Integer penaltyHoursBefore;
    private BigDecimal penaltyAmount;
    private Boolean requiresApproval;

    @OneToOne
    @JoinColumn(name = "common_area_id")
    private CommonArea commonArea;

    public CommonAreaRule(Integer maxReservationHours, Boolean requiresPayment, BigDecimal price, Boolean requiresGuarantee, BigDecimal guaranteeAmount, Boolean allowCancellation, Integer penaltyHoursBefore, BigDecimal penaltyAmount, Boolean requiresApproval, CommonArea commonArea) {
        this.maxReservationHours = maxReservationHours;
        this.requiresPayment = requiresPayment;
        this.price = price;
        this.requiresGuarantee = requiresGuarantee;
        this.guaranteeAmount = guaranteeAmount;
        this.allowCancellation = allowCancellation;
        this.penaltyHoursBefore = penaltyHoursBefore;
        this.penaltyAmount = penaltyAmount;
        this.requiresApproval = requiresApproval;
        this.commonArea = commonArea;
    }

    public void setCommonArea(CommonArea commonArea) {
        this.commonArea = commonArea;
    }
}
