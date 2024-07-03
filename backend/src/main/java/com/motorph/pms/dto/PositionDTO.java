package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record PositionDTO (
        String positionCode,
        String departmentCode,
        boolean isLeader,
        String positionName
) {
}
