package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record BenefitTypeDTO(
        int benefitTypeId,
        String benefit
) {}
