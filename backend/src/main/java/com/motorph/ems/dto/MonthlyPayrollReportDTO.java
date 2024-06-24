package com.motorph.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor
public class MonthlyPayrollReportDTO {
    private LocalDate month;
    private double totalEarnings;
    private double totalDeductions;
}
