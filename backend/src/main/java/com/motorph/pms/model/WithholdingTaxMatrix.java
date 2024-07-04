package com.motorph.pms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "withholding_tax")
public class WithholdingTaxMatrix {

    @Id
    private Long id;
    private Double minRange;
    private Double maxRange;
    private Double baseTax;
    private Double taxRate;
    private Double excess;
}
