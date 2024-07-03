package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record UserAuth (
         Long employeeId,
         Long roleId
) {
}
