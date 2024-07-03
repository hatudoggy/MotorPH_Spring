package com.motorph.pms.dto;

import com.motorph.pms.model.Deductions.DeductionType;
import lombok.Builder;

@Builder
public record DeductionsDTO(
        Long deductionId,
        Long payrollId,
        DeductionType deductionType,
        double amount
) {}
