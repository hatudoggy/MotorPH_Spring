package com.motorph.ems.dto;

import com.motorph.ems.model.Deductions;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class PayrollDTO {
    private Long payrollId;
    private Long employeeId;
    private String employeeFirstName;
    private String employeeLastName;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private double monthlyRate;
    private double dailyRate;
    private double overtimePay;
    private double grossIncome;
    private double netIncome;
    private List<Deductions> deductions;
}
