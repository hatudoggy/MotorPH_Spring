package com.motorph.pms.service;

import com.motorph.pms.dto.AttendanceDTO;
import com.motorph.pms.dto.AttendanceSummaryDTO;
import com.motorph.pms.model.Attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface AttendanceService {
     AttendanceDTO addNewAttendance(Attendance attendance);

     List<AttendanceDTO> getAllAttendances();

     List<AttendanceDTO> getAllByEmployeeId(Long employeeId);

     Optional<AttendanceDTO> getAttendanceById(Long attendanceId);

     Optional<AttendanceDTO> getAttendanceByEmployeeIdAndDate(Long employeeId, LocalDate date);

     List<AttendanceDTO> getAllByDate(LocalDate date);

     AttendanceDTO updateAttendance(Long employeeId, AttendanceDTO attendance);

    List<AttendanceDTO> getAllAttendanceByEmployeeIdAndDateRange(Long id, LocalDate start, LocalDate end);
}
