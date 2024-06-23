package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record LeaveStatusDTO(
        int id,
        String status
) {}
