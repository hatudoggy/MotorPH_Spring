package com.motorph.ems.controller;

import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.Employee;
import com.motorph.ems.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping()
    public List<Attendance> getAttendances(
        @RequestParam(value = "date", required = false) String date
    ) {
        if(date != null){
            return attendanceService.getAllAttendancesByDate(LocalDate.parse(date));
        } else {
            return attendanceService.getAllAttendances();
        }
    }

    @GetMapping("/{id}")
    public Attendance getAttendanceById(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "date", required = false) String date
    ) {
        if(date != null) {
            return attendanceService.getAttendanceByEmployeeIdAndDate(id, LocalDate.parse(date));
        } else {
            return attendanceService.getAttendanceById(id);
        }
    }

    @PatchMapping("/{id}")
    public void updateAttendance(
            @PathVariable(value = "id") Long employeeID,
            @RequestBody Attendance attendance
    ) {
        attendanceService.updateAttendance(attendance);
    }

    @PostMapping
    public void addAttendance(@RequestBody Attendance attendance) {
        attendanceService.addNewAttendance(attendance);
    }
}
