package com.motorph.pms.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MonthlyPayrollReportDTO (
        LocalDate month,
        double totalEarnings,
        double totalDeductions
){
}
