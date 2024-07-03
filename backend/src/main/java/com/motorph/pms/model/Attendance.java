package com.motorph.pms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double totalHours;
    private double overtimeHours;

    public Attendance(
            Long employeeId,
            LocalDate date,
            LocalTime timeIn
    ) {
        this.employee = new Employee(employeeId);
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = null;
        this.totalHours = 0;
        this.overtimeHours = 0;
    }

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
        this.totalHours = calculateTotalHours();
        this.overtimeHours = calculateOvertime();
    }



    //May add new field for boolean paid overtime

    public double calculateTotalHours() {
        if (timeIn == null || timeOut == null) {
            return 0;
        }
        return secondsToHours(timeOut.toSecondOfDay() - timeIn.toSecondOfDay());
    }

    public double calculateOvertime() {
        if (timeIn == null || timeOut == null) {
            return 0;
        }

        long outCutOff = LocalTime.of(17, 0).toSecondOfDay(); // Assuming 17:00 (5 PM) as the cutoff for overtime

        if (timeOut.toSecondOfDay() < outCutOff) {
            return 0;
        }

        return timeOut.toSecondOfDay() - outCutOff;
    }

    private double secondsToHours(long seconds) {
        return seconds / 3600.0; // Convert seconds to hours
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
