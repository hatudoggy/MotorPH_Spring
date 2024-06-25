package com.motorph.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
public record MonthlyPayrollReportDTO (
        LocalDate month,
        double totalEarnings,
        double totalDeductions
){
}
