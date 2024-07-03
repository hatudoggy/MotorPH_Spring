package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record EmploymentStatusDTO (
        int statusId,
        String statusName
){}
