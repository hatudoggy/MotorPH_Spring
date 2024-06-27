package com.motorph.ems.controller;

import com.motorph.ems.dto.AttendanceDTO;
import com.motorph.ems.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping()
    public ResponseEntity<List<AttendanceDTO>> getAttendances(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "id", required = false) Long id
    ) {

        List<AttendanceDTO> attendances;
        if(date != null){
            if(id != null && !id.equals(0L)){
                attendances = attendanceService.getAllByEmployeeId(id);
            } else {
                attendances = attendanceService.getAllByDate(LocalDate.parse(date));
            }
        } else {
            attendances = attendanceService.getAllAttendances();
        }

        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> getAttendanceById(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "date", required = false) String date
    ) {
        Optional<AttendanceDTO> attendance;

        if(date != null) {
            attendance = attendanceService.getAttendanceByEmployeeIdAndDate(id, LocalDate.parse(date));
        } else {
            attendance = attendanceService.getAttendanceById(id);
        }

        if (attendance.isEmpty()) {
            throw new IllegalStateException("Attendance with id " + id + " not found");
        }

        return ResponseEntity.ok(attendance.get());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AttendanceDTO> updateAttendance(
            @PathVariable(value = "id") Long attendanceId,
            @RequestBody AttendanceDTO attendance
    ) {
        AttendanceDTO updatedAttendance = attendanceService.updateAttendance(attendanceId, attendance);

        return ResponseEntity.ok(updatedAttendance);
    }

    @PostMapping
    public ResponseEntity<AttendanceDTO> addAttendance(@RequestBody AttendanceDTO attendance) {
        AttendanceDTO attendanceDTO = attendanceService.addNewAttendance(attendance);

        return ResponseEntity.ok(attendanceDTO);
    }
}
