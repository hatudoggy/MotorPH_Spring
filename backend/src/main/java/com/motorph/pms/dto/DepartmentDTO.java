package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record DepartmentDTO (
         String departmentCode,
         String departmentName
) {}
