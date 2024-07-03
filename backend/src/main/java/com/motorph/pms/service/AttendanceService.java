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

     List<AttendanceDTO> getAttendancesForDateRange(LocalDate start, LocalDate end);

     List<AttendanceDTO> getAttendancesAfterTimeIn(LocalTime timeIn, LocalDate date);

     List<AttendanceDTO> getAttendancesByDateAndEmployeeName(LocalDate date, String name);

    public AttendanceSummaryDTO getAttendanceSummaryByEmployeeId(Long employeeId);

     List<AttendanceDTO> getAttendancesAfterTimeOut(LocalTime timeOut, LocalDate date);

     AttendanceDTO updateAttendance(Long employeeId, AttendanceDTO attendance);

     void deleteAttendanceById(Long attendanceId);

    List<AttendanceDTO> getAllAttendanceByEmployeeIdAndDateRange(Long id, LocalDate start, LocalDate end);
}
