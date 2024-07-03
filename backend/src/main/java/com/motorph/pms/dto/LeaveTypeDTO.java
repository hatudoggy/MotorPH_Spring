package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record LeaveTypeDTO(
    int id,
    String typeName
) {}
