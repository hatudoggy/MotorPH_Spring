package com.motorph.ems.controller;

import com.motorph.ems.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

//    @RequestMapping
//    public List<> getAttendances() {
//        return attendanceService.getAllAttendances();
//    }
//
//    @RequestMapping("/{employee}")
//    public List<Attendance> getAttendancesByEmployeeId(@PathVariable Long employee) {
//        return attendanceService.getAllAttendancesByEmployeeId(employee);
//    }
}
