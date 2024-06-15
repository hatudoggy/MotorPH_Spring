package com.motorph.ems.service;

import com.motorph.ems.model.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    public void addNewAttendance(Attendance attendance);

    public List<Attendance> getAllAttendances();

    public List<Attendance> getAllAttendancesByEmployeeId(Long employeeId);

    public Attendance getAttendanceById(Long attendanceId);

    public Attendance getAttendanceByEmployeeIdAndDate(Long employeeId, LocalDate date);

    public void updateAttendance(Attendance attendance);
    public void deleteAttendance(Long attendanceId);

}
