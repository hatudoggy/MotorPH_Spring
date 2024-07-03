package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record BenefitDTO(
    Long benefitId,
    Long employeeId,
    BenefitTypeDTO benefitType,
    Double amount
) {}
