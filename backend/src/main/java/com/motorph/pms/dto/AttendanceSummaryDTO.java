package com.motorph.ems.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
