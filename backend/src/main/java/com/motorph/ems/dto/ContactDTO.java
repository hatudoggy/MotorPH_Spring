package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record ContactDTO(
        Long contactId,
        Long employeeId,
        String contactNo
) {}
