package com.motorph.ems.dto;

import com.motorph.ems.model.Deductions.DeductionType;
import lombok.Builder;

@Builder
public record DeductionsDTO(
        Long deductionId,
        Long payrollId,
        DeductionType deductionType,
        double amount
) {}
