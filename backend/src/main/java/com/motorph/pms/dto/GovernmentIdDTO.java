package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record GovernmentIdDTO (
         Long id,
         String sssNo,
         String philHealthNo,
         String pagIbigNo,
         String tinNo
) {}
