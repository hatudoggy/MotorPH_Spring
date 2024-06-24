package com.motorph.ems.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonManagedReference
    private Employee employee;

    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private int hoursWorked;
    private int overtime;

    public Attendance() {}

    public Attendance(
            LocalDate date,
            LocalTime timeIn,
            LocalTime timeOut,
            int hoursWorked,
            int overtime
    ) {
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.hoursWorked = hoursWorked;
        this.overtime = overtime;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", date=" + date +
                ", timeIn=" + timeIn +
                ", timeOut=" + timeOut +
                '}';
    }

}
