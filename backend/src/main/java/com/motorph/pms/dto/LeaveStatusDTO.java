package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record LeaveStatusDTO(
        int id,
        String status
) {}
