package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record DeductionTypeDTO(
        String deductionCode,
        String name
) {}
