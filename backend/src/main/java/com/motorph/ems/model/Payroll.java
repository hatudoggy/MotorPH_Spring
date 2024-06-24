package com.motorph.ems.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@Entity @Table(name = "payroll")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonManagedReference
    private Employee employee;

    private LocalDate periodStart;
    private LocalDate periodEnd;
    private double monthlyRate;
    private double dailyRate;
    private double overtimePay;
    private double grossIncome;
    private double netPay;
    private double netIncome;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Deductions> deductions;


    public Payroll() {}

    public Payroll(LocalDate periodStart, LocalDate periodEnd, double monthlyRate, double dailyRate, double overtimePay, double grossIncome, double netPay, double netIncome) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.monthlyRate = monthlyRate;
        this.dailyRate = dailyRate;
        this.overtimePay = overtimePay;
        this.grossIncome = grossIncome;
        this.netPay = netPay;
        this.netIncome = netIncome;
    }

    @Override
    public String toString() {
        return "Payroll{" +
                "payrollId=" + payrollId +
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
