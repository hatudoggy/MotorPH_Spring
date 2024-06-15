package com.motorph.ems.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Entity @Table(name = "payroll")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    private Long employeeId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private double monthlyRate;
    private double dailyRate;
    private double overtimePay;
    private double grossIncome;
    private double netIncome;

    public Payroll() {}

    public Payroll(Long employeeId, LocalDate periodStart, LocalDate periodEnd, double monthlyRate, double dailyRate, double overtimePay, double grossIncome, double netIncome) {
        this.employeeId = employeeId;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.monthlyRate = monthlyRate;
        this.dailyRate = dailyRate;
        this.overtimePay = overtimePay;
        this.grossIncome = grossIncome;
        this.netIncome = netIncome;
    }

    @Override
    public String toString() {
        return "Payroll{" +
                "payrollId=" + payrollId +
                ", employeeId=" + employeeId +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", monthlyRate=" + monthlyRate +
                ", dailyRate=" + dailyRate +
                ", overtimePay=" + overtimePay +
                ", grossIncome=" + grossIncome +
                ", netIncome=" + netIncome +
                '}';
    }
}
