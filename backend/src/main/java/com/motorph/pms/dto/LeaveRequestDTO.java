package com.motorph.pms.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LeaveRequestDTO(
    Long leaveRequestId,
    EmployeeDTO employee,
    LeaveTypeDTO leaveType,
    LocalDate requestDate,
    LocalDate startDate,
    LocalDate endDate,
    int daysRequested,
    LeaveStatusDTO status,
    String reason
) {}
