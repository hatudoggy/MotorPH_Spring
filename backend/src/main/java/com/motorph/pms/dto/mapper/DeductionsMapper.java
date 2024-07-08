package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.DeductionTypeDTO;
import com.motorph.pms.dto.DeductionsDTO;
import com.motorph.pms.model.Deductions;
import com.motorph.pms.model.Deductions.DeductionType;
import com.motorph.pms.model.Payroll;
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
                .deductionType(toDTO(entity.getDeductionType()))
                .amount(entity.getAmount())
                .build();
    }

    public Deductions toEntity(DeductionsDTO dto, Payroll payroll) {
        if (dto.payrollId() == null) {
            throw new IllegalArgumentException("Payroll ID cannot be null when creating deduction");
        }

        return new Deductions(
                payroll,
                dto.deductionType().deductionCode(),
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

    public List<Deductions> toEntity(List<DeductionsDTO> dto, Payroll payroll) {
        if (dto == null) {
            return null;
        }

        return dto.stream()
                .map(dto1 -> toEntity(dto1, payroll))
                .collect(Collectors.toList());
    }

    public DeductionTypeDTO toDTO(DeductionType entity) {
        if (entity == null) {
            return null;
        }
        return DeductionTypeDTO.builder()
                .deductionCode(entity.getDeductionCode())
                .name(entity.getName())
                .build();
    }
}
