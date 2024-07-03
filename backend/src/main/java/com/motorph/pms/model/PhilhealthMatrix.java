package com.motorph.ems.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "philhealth_matrix")
public class PhilhealthMatrix {
    @Id
    private Long matrixId;
    private Double minRange;
    private Double maxRange;
    private Double premiumRate;
    private Long monthlyPremiumBase;
    private Long monthlyPremiumCap;
}
