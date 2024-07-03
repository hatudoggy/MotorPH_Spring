package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record PositionDTO (
        String positionCode,
        String departmentCode,
        boolean isLeader,
        String positionName
) {
}
