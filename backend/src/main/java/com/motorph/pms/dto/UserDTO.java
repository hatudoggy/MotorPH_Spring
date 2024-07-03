package com.motorph.pms.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDTO(
        Long userId,
        Long employeeId,
        String employeeName,
        String password,
        String positionCode,
        String departmentCode,
        int roleId,
        String username,
        LocalDateTime createdAt,
        LocalDateTime lastModified
) {}
