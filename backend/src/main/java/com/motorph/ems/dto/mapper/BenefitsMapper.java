package com.motorph.ems.dto.mapper;

import com.motorph.ems.dto.BenefitDTO;
import com.motorph.ems.dto.BenefitTypeDTO;
import com.motorph.ems.model.Benefits;
import com.motorph.ems.model.Benefits.BenefitType;
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

        return new Benefits(
                benefitDTO.employeeId(),
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

    public void updateFromDTO(BenefitDTO benefitDTO, Benefits benefit) {
        if (benefitDTO.employeeId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when updating benefit");
        }

        if (!benefitDTO.employeeId().equals(benefit.getEmployee().getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID does not match when updating benefit");
        }

        benefit.setAmount(benefitDTO.amount());
    }

    public List<BenefitDTO> toDTO(List<Benefits> benefits) {
        return benefits.stream().map(this::toDTO).toList();
    }

    public List<Benefits> toEntity(List<BenefitDTO> benefitDTOs) {
        return benefitDTOs.stream().map(this::toEntity).toList();
    }
}
