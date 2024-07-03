package com.motorph.pms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "pagibig_matrix")
public class PagibigMatrix {
    @Id
    private Long matrixId;
    private Long minRange;
    private Long maxRange;
    private Double employeeRate;
    private Double employerRate;

    @Transient
    private Double totalRate;

    public Double getTotalRate() {
        return this.employeeRate + this.employerRate;
    }
}
