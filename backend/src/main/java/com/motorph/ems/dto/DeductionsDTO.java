package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record DeductionsDTO(
        Long deductionId,
        Long payrollId,
        String deductionCode,
        double amount
) {}
