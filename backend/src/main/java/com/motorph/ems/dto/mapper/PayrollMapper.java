package com.motorph.ems.dto.mapper;

import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.Payroll;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PayrollMapper {

    private final DeductionsMapper deductionsMapper;

    public PayrollMapper(DeductionsMapper deductionsMapper) {
        this.deductionsMapper = deductionsMapper;
    }

    public PayrollDTO toDTO(Payroll entity) {
        if (entity == null) {
            return null;
        }

        return PayrollDTO.builder()
                .payrollId(entity.getPayrollId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .periodStart(entity.getPeriodStart())
                .periodEnd(entity.getPeriodEnd())
                .monthlyRate(entity.getMonthlyRate())
                .dailyRate(entity.getDailyRate())
                .overtimePay(entity.getOvertimePay())
                .deductions(entity.getDeductions().stream()
                        .map(deductionsMapper::toDTO)
                        .collect(Collectors.toList()))
                .grossIncome(entity.getGrossIncome())
                .netIncome(entity.getNetIncome())
                .build();
    }

    public Payroll toEntity(PayrollDTO dto) {
        if (dto == null || dto.employeeId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when creating payroll");
        }

        if (dto.deductions() == null) {
            throw new IllegalArgumentException("Deductions cannot be null when creating payroll");
        }

        return new Payroll(
                dto.employeeId(),
                dto.periodStart(),
                dto.periodEnd(),
                dto.monthlyRate(),
                dto.dailyRate(),
                dto.overtimePay(),
                dto.grossIncome(),
                dto.netIncome()
        );
    }

    public List<PayrollDTO> toDTO(List<Payroll> entity) {
        if (entity == null) {
            return null;
        }

        return entity.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void updateEntity(PayrollDTO payrollDTO, Payroll entity) {
        if (entity == null || entity.getPayrollId() == null) {
            throw new IllegalArgumentException("Payroll ID cannot be null when updating a payroll");
        }

        if (!entity.getEmployee().getEmployeeId().equals(payrollDTO.employeeId())) {
            throw new IllegalArgumentException("Employee ID cannot be changed when updating a payroll");
        }

        entity.setDeductions(deductionsMapper.toEntity(payrollDTO.deductions()));
        entity.setPeriodStart(payrollDTO.periodStart());
        entity.setPeriodEnd(payrollDTO.periodEnd());
        entity.setMonthlyRate(payrollDTO.monthlyRate());
        entity.setDailyRate(payrollDTO.dailyRate());
        entity.setOvertimePay(payrollDTO.overtimePay());
        entity.setGrossIncome(payrollDTO.grossIncome());
        entity.setNetIncome(payrollDTO.netIncome());
    }
}
