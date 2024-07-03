package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record BenefitTypeDTO(
        int benefitTypeId,
        String benefit
) {}
