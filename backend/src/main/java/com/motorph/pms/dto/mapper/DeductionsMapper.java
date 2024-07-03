package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.DeductionsDTO;
import com.motorph.pms.model.Deductions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeductionsMapper {

    public DeductionsDTO toDTO(Deductions entity) {
        if (entity == null || entity.getPayroll() == null) {
            return null;
        }

        return DeductionsDTO.builder()
                .deductionId(entity.getDeductionId())
                .payrollId(entity.getPayroll().getPayrollId())
                .deductionType(entity.getDeductionType())
                .amount(entity.getAmount())
                .build();
    }

    public Deductions toEntity(DeductionsDTO dto) {
        if (dto.payrollId() == null) {
            throw new IllegalArgumentException("Payroll ID cannot be null when creating deduction");
        }

        return new Deductions(
                dto.payrollId(),
                dto.deductionType().getDeductionCode(),
                dto.amount()
        );
    }

    public List<DeductionsDTO> toDTO(List<Deductions> entity) {
        if (entity == null) {
            return null;
        }

        return entity.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Deductions> toEntity(List<DeductionsDTO> dto) {
        return dto.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public void updateFromDTO(DeductionsDTO dto, Deductions entity) {
        if (dto.payrollId() == null) {
            throw new IllegalArgumentException("Payroll ID cannot be null when updating deduction");
        }

        if (!dto.payrollId().equals(entity.getPayroll().getPayrollId())) {
            throw new IllegalArgumentException("Payroll ID does not match when updating deduction");
        }

        entity.setAmount(dto.amount());
    }
}
