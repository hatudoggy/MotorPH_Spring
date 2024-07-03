package com.motorph.ems.model;

import com.motorph.ems.dto.BenefitDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity @Table(name = "payroll")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "payroll_id")
    private List<Deductions> deductions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private List<Benefits> benefits;

    private LocalDate payDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private int workingDays;
    private int daysWorked;
    private double monthlyRate;
    private double hoursWorked;
    private double hourlyRate;
    private double overtimeHours;
    private double overtimeRate;
    private double grossIncome;
    private double totalBenefits;
    private double totalDeductions;
    private double netPay;

    @Transient
    private double overtimePay;

    public Payroll(
            Long employeeId,
            LocalDate periodStart,
            LocalDate periodEnd,
            int daysWorked,
            double monthlyRate,
            double hourlyRate,
            double hoursWorked,
            double overtimeHours,
            double overtimeRate) {
        this.employee = new Employee(employeeId);
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.workingDays = calculateWorkingDays(periodStart,periodEnd);
        this.daysWorked = daysWorked;
        this.monthlyRate = monthlyRate;
        this.hoursWorked = hoursWorked;
        this.hourlyRate  = hourlyRate;
        this.overtimeRate = overtimeRate;
        this.overtimePay = calculateOvertimePay(overtimeHours,overtimeRate);
        this.overtimeHours = overtimeHours;
    }

    public Payroll(Long payrollId) {}

    public int calculateWorkingDays(LocalDate periodStart, LocalDate periodEnd) {
        int workingDays = 0;
        LocalDate date = periodStart;

        while (!date.isAfter(periodEnd)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            date = date.plusDays(1);
        }

        return workingDays;
    }


    public double calculateOvertimePay(double overtimeHours, double overtimeRate) {
        if (overtimeHours > 0){
            return Math.round(overtimeHours * overtimeRate * 100) / 100.0;
        }

        return 0.0;
    }


    @Override
    public String toString() {
        return "Payroll{" +
                "payrollId=" + payrollId +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", monthlyRate=" + monthlyRate +
                ", overtimePay=" + overtimePay +
                ", grossIncome=" + grossIncome +
                ", netPay=" + netPay +
                '}';
    }
}
