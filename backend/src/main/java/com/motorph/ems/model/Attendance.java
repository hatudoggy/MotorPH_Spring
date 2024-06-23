package com.motorph.ems.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity @Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;

    public Attendance(
            Long employeeId,
            LocalDate date,
            LocalTime timeIn,
            LocalTime timeOut
    ) {
        this.employee = new Employee(employeeId);
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    @Transient
    private Duration totalHours;

    @Transient
    private Duration overtimeHours;

    //May add new field for boolean paid overtime

    public double getTotalHours() {
        return timeOut.toSecondOfDay() - timeIn.toSecondOfDay();
    }

    public double getOvertimeHours() {
        long overtimeHours = LocalTime.of(17,0).toSecondOfDay();
        return timeOut.toSecondOfDay() < overtimeHours ? 0 : timeOut.toSecondOfDay() - timeIn.toSecondOfDay();
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
