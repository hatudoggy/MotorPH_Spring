package com.motorph.ems.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AttendanceDTO (
        Long attendanceId,
        EmployeeDTO employee,
        LocalDate date,
        LocalTime timeIn,
        LocalTime timeOut,
        double totalHours,
        double overtimeHours
) {

    public AttendanceDTO withTimeOut(LocalTime timeOut) {
        return AttendanceDTO.builder()
                .attendanceId(attendanceId)
                .employee(employee)
                .date(date)
                .timeIn(timeIn)
                .timeOut(timeOut)
                .totalHours(totalHours)
                .overtimeHours(overtimeHours)
                .build();
    }
}
