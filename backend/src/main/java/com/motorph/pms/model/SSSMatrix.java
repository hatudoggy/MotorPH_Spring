package com.motorph.pms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "sss_matrix")
public class SSSMatrix {
    @Id
    private Long matrixId;
    private Long minRange;
    private Long maxRange;
    private Long contribution;
}
