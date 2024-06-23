package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record LeaveTypeDTO(
    int id,
    String typeName
) {}
