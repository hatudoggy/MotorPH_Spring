package com.motorph.ems.dto.mapper;

import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.Payroll;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PayrollMapper {

    private final EmployeeMapper employeeMapper;
    private final DeductionsMapper deductionsMapper;
    private final BenefitsMapper benefitsMapper;

    public PayrollMapper(EmployeeMapper employeeMapper, DeductionsMapper deductionsMapper,
                         BenefitsMapper benefitsMapper) {
        this.employeeMapper = employeeMapper;
        this.deductionsMapper = deductionsMapper;
        this.benefitsMapper = benefitsMapper;
    }

    public PayrollDTO toLimitedDTO(Payroll entity) {
        if (entity == null) {
            return null;
        }

        return PayrollDTO.builder()
                .payrollId(entity.getPayrollId())
                .employee(employeeMapper.toLimitedDTO(entity.getEmployee()))
                .monthlyRate(entity.getMonthlyRate())
                .grossIncome(entity.getGrossIncome())
                .totalBenefits(entity.getTotalBenefits())
                .totalDeductions(entity.getTotalDeductions())
                .netPay(entity.getNetPay())
                .build();
    }

    public PayrollDTO toDTO(Payroll entity) {
        if (entity == null) {
            return null;
        }

        return PayrollDTO.builder()
                .payrollId(entity.getPayrollId())
                .payDate(entity.getPayDate())
                .employee(employeeMapper.toLimitedDTO(entity.getEmployee()))
                .periodStart(entity.getPeriodStart())
                .periodEnd(entity.getPeriodEnd())
                .workingDays(entity.getWorkingDays())
                .daysWorked(entity.getDaysWorked())
                .monthlyRate(entity.getMonthlyRate())
                .hoursWorked(entity.getHoursWorked())
                .hourlyRate(entity.getHourlyRate())
                .overtimeHours(entity.getOvertimeHours())
                .overtimeRate(entity.getOvertimeRate())
                .overtimePay(entity.getOvertimePay())
                .grossIncome(entity.getGrossIncome())
                .totalBenefits(entity.getTotalBenefits())
                .totalDeductions(entity.getTotalDeductions())
                .netPay(entity.getNetPay())
                .deductions(entity.getDeductions().stream()
                        .map(deductionsMapper::toDTO)
                        .collect(Collectors.toList()))
                .benefits(entity.getBenefits().stream()
                        .map(benefitsMapper::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public Payroll toEntity(PayrollDTO dto) {
        if (dto == null || dto.employee() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when creating payroll");
        }

        if (dto.deductions() == null) {
            throw new IllegalArgumentException("Deductions cannot be null when creating payroll");
        }

        return new Payroll(
                dto.employee().employeeId(),
                dto.periodStart(),
                dto.periodEnd(),
                dto.daysWorked(),
                dto.monthlyRate(),
                dto.hourlyRate(),
                dto.hoursWorked(),
                dto.overtimeHours(),
                dto.overtimeRate()
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

        if (!entity.getEmployee().getEmployeeId().equals(payrollDTO.employee().employeeId())) {
            throw new IllegalArgumentException("Employee ID cannot be changed when updating a payroll");
        }

        entity.setDeductions(deductionsMapper.toEntity(payrollDTO.deductions()));
        entity.setPeriodStart(payrollDTO.periodStart());
        entity.setPeriodEnd(payrollDTO.periodEnd());
        entity.setMonthlyRate(payrollDTO.monthlyRate());
        entity.setHourlyRate(payrollDTO.hourlyRate());
        entity.setOvertimePay(payrollDTO.overtimePay());
        entity.setGrossIncome(payrollDTO.grossIncome());
        entity.setNetPay(payrollDTO.netPay());
    }
}
