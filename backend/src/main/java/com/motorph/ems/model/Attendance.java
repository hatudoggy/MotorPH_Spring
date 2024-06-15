package com.motorph.ems.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private Employee employee;

    private LocalDate workDate;

    private LocalDateTime clockIn;

    private LocalDateTime clockOut;

    private int overtime;

    private int hoursWorked;

    @Transient
    private String status;
}
