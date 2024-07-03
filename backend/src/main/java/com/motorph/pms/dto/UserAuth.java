package com.motorph.ems.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record UserAuth (
         Long employeeId,
         Long roleId
) {
}
