package com.motorph.ems.controller;

import com.motorph.ems.model.Attendance;
import com.motorph.ems.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @RequestMapping
    public List<Attendance> getAttendances() {
        return attendanceService.getAllAttendances();
    }

    @RequestMapping("/{employeeId}")
    public List<Attendance> getAttendancesByEmployeeId(@PathVariable Long employeeId) {
        return attendanceService.getAllAttendancesByEmployeeId(employeeId);
    }
}
