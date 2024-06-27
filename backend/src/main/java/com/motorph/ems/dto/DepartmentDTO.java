package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record DepartmentDTO (
         String departmentCode,
         String departmentName
) {}
