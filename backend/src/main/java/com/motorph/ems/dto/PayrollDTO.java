package com.motorph.ems.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PayrollDTO (
        Long payrollId,
        Long employeeId,
        LocalDate periodStart,
        LocalDate periodEnd,
        double monthlyRate,
        double dailyRate,
        double overtimePay,
        List<DeductionsDTO> deductions,
        List<BenefitDTO> benefits,
        double grossIncome,
        double netIncome
){}
