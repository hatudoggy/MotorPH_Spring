package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record GovernmentIdDTO (
         Long id,
         Long employeeId,
         String sssNo,
         String philHealthNo,
         String pagIbigNo,
         String tinNo
) {}
