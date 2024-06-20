package com.motorph.ems.service;

import com.motorph.ems.dto.AttendanceSummary;
import com.motorph.ems.model.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    public Attendance addNewAttendance(Attendance attendance);

    public List<Attendance> getAllAttendances();

    public List<Attendance> getAllAttendancesByDate(LocalDate localDate);

    public List<Attendance> getAllAttendancesByEmployeeId(Long employeeId);

    public Attendance getAttendanceById(Long attendanceId);

    public Attendance getAttendanceByEmployeeIdAndDate(Long employeeId, LocalDate date);

    public AttendanceSummary getAttendanceSummaryByEmployeeId(Long employeeId);

    public Attendance updateAttendance(Attendance attendance);
    public void deleteAttendance(Long attendanceId);

}
