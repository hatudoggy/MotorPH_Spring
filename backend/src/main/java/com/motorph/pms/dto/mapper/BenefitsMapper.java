package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.BenefitDTO;
import com.motorph.pms.dto.BenefitTypeDTO;
import com.motorph.pms.model.Benefits;
import com.motorph.pms.model.Benefits.BenefitType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component public class BenefitsMapper {

    public BenefitDTO toDTO(Benefits benefit) {
        if (benefit == null || benefit.getEmployee() == null) {
            return null;
        }

        return BenefitDTO.builder()
                .benefitId(benefit.getBenefitId())
                .employeeId(benefit.getEmployee().getEmployeeId())
                .benefitType(toDTO(benefit.getBenefitType()))
                .amount(benefit.getAmount())
                .build();
    }

    public Benefits toEntity(BenefitDTO benefitDTO) {
        if (benefitDTO == null) {
            return null;
        }

        if (benefitDTO.benefitType() == null) {
            throw new IllegalArgumentException("Benefit type cannot be null when creating benefit");
        }

        return new Benefits(
                benefitDTO.benefitType().benefitTypeId(),
                benefitDTO.amount());
    }

    public BenefitTypeDTO toDTO(BenefitType benefit) {
        if (benefit == null) {
            return null;
        }

        return BenefitTypeDTO.builder()
                .benefitTypeId(benefit.getBenefitTypeId())
                .benefit(benefit.getBenefit())
                .build();
    }

    public BenefitType toEntity(BenefitTypeDTO benefitDTO) {
        if (benefitDTO == null) {
            return null;
        }

        return new BenefitType(
                benefitDTO.benefitTypeId(),
                benefitDTO.benefit());
    }

    public List<BenefitDTO> toDTO(List<Benefits> benefits) {
        if (benefits == null) {
            return null;
        }

        return benefits.stream().map(this::toDTO).toList();
    }

    public List<Benefits> toEntity(List<BenefitDTO> benefitDTOs) {
        return benefitDTOs.stream().map(this::toEntity).toList();
    }
}
