package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record PositionDTO (
        String positionCode,
        String departmentCode,
        String positionName
) {}
