package com.motorph.ems.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "deductions")
public class Deductions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deductionId;

    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payrollId")
    @JsonBackReference
    private Payroll payroll;

    @ManyToOne
    @JoinColumn(name = "deduction_code", nullable = false, columnDefinition = "VARCHAR(10)")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Deductions.DeductionType deductionType;

    public Deductions() {}

    @Getter @Setter
    @Entity @Table(name = "deduction_type")
    public static class DeductionType {
        @Id
        @Column(name = "deduction_code", columnDefinition = "VARCHAR(10)")
        private String deductionCode;
        private String name;
        private String description;
    }
}
