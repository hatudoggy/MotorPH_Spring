package com.motorph.ems.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
public class AttendanceSummary {

    private int totalCount;
    private int presentCount;
    private int lateCount;
    private int absentCount;
    private LocalTime averageTimeIn;
    private LocalTime averageTimeOut;

}
