package com.motorph.pms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deductions")
public class Deductions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deductionId;

    @ManyToOne
    @JoinColumn(name = "payroll_id")
    private Payroll payroll;

    @ManyToOne
    @JoinColumn(name = "deduction_code")
    private DeductionType deductionType;

    private double amount;

    public Deductions(
            Long payrollId,
            String deductionCode,
            double amount) {
        this.payroll = new Payroll(payrollId);
        this.deductionType = new DeductionType(deductionCode);
        this.amount = amount;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "deduction_type")
    public static class DeductionType {
        @Id
        private String deductionCode;
        private String name;

        public DeductionType(String deductionCode) {}
    }
}
