package com.motorph.ems.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LeaveRequestDTO(
    Long leaveRequestId,
    Long employeeId,
    LocalDate requestDate,
    LocalDate startDate,
    LocalDate endDate,
    int daysRequested,
    int statusId,
    String reason
) {}
