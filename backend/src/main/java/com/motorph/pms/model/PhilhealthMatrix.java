package com.motorph.pms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "philhealth_matrix")
public class PhilhealthMatrix {
    @Id
    private Long id;
    private Double premiumRate;
    private Double employeeShare;
    private Double premiumCap;
}
