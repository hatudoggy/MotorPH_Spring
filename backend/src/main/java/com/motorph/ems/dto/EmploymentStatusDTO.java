package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record EmploymentStatusDTO (
        int statusId,
        String statusName
){}
