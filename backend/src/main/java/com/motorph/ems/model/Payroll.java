package com.motorph.ems.model;

import com.motorph.ems.dto.BenefitDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany
    @JoinColumn(name = "payroll_id")
    private List<Deductions> deductions;

    private LocalDate periodStart;
    private LocalDate periodEnd;
    private double monthlyRate;
    private double dailyRate;
    private double overtimePay;

    @Transient
    private List<BenefitDTO> benefits;

    private double grossIncome;
    private double netIncome;

    public Payroll(
            Long employeeId,
            List<Deductions> deductions,
            LocalDate periodStart,
            LocalDate periodEnd,
            double monthlyRate,
            double dailyRate,
            double overtimePay,
            double grossIncome,
            double netIncome) {
        this.employee = new Employee(employeeId);
        this.deductions = deductions;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.monthlyRate = monthlyRate;
        this.dailyRate = dailyRate;
        this.overtimePay = overtimePay;
        this.grossIncome = grossIncome;
        this.netIncome = netIncome;
    }

    public Payroll(Long payrollId) {}

    @Override
    public String toString() {
        return "Payroll{" +
                "payrollId=" + payrollId +
                ", supervisorId=" + employee.getEmployeeId() +
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
