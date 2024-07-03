package com.motorph.pms.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PayrollDTO (
        Long payrollId,
        LocalDate payDate,
        EmployeeDTO employee,
        LocalDate periodStart,
        LocalDate periodEnd,
        int workingDays,
        int daysWorked,
        double monthlyRate,
        double hoursWorked,
        double hourlyRate,
        double overtimeHours,
        double overtimeRate,
        double overtimePay,
        double grossIncome,
        double totalBenefits,
        double totalDeductions,
        double netPay,
        List<DeductionsDTO> deductions,
        List<BenefitDTO> benefits
){}
