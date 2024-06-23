package com.motorph.ems.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AttendanceDTO (
        Long attendanceId,
        Long employeeId,
        LocalDate date,
        LocalTime timeIn,
        LocalTime timeOut,
        double totalHours,
        double overtimeHours
) {}
