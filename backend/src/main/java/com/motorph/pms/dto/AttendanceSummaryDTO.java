package com.motorph.pms.dto;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record AttendanceSummaryDTO (
        int totalCount,
        int presentCount,
        int lateCount,
        int absentCount,
        LocalTime averageTimeIn,
        LocalTime averageTimeOut
){
}
