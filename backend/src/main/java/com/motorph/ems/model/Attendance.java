package com.motorph.ems.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@Entity @Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private Long employeeId;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;

    public Attendance() {}

    public Attendance(
            Long employeeId,
            LocalDate date,
            LocalTime timeIn,
            LocalTime timeOut
    ) {
        this.employeeId = employeeId;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", employeeId=" + employeeId +
                ", date=" + date +
                ", timeIn=" + timeIn +
                ", timeOut=" + timeOut +
                '}';
    }

}
