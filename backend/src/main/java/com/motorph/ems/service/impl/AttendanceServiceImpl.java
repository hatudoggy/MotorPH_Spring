package com.motorph.ems.service.impl;

import com.motorph.ems.dto.AttendanceSummary;
import com.motorph.ems.model.Attendance;
import com.motorph.ems.repository.AttendanceRepository;
import com.motorph.ems.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Attendance addNewAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll(Sort.by("date"));
    }

    @Override
    public List<Attendance> getAllAttendancesByDate(LocalDate localDate) {
        return attendanceRepository.findByDate(localDate);
    }

    @Override
    public List<Attendance> getAllAttendancesByDateAndNameContains(LocalDate localDate, String name) {
        return attendanceRepository.findByDateAndNameContaining(localDate, name);
    }

    @Override
    public List<Attendance> getAllAttendancesByEmployeeId(Long employeeId) {
        return attendanceRepository.findByEmployeeEmployeeIdOrderByDateDesc(employeeId);
    }

    @Override
    public Attendance getAttendanceById(Long attendanceId) {
        return attendanceRepository.findById(attendanceId).orElse(null);
    }

    @Override
    public Attendance getAttendanceByEmployeeIdAndDate(Long employeeId, LocalDate date) {
        return attendanceRepository.findByEmployeeEmployeeIdAndDate(employeeId, date).orElse(null);
    }

    @Override
    public AttendanceSummary getAttendanceSummaryByEmployeeId(Long employeeId) {
        List<Attendance> attendanceList = attendanceRepository.findByEmployeeEmployeeIdOrderByDateDesc(employeeId);

        int totalCount = attendanceList.size();
        int lateCount = (int) attendanceList.stream().filter(a -> a.getTimeIn().isAfter(LocalTime.of(8,0))).count();
        int absentCount = (int) attendanceList.stream().filter(a -> a.getTimeIn() == null && a.getTimeOut() == null).count();

        double averageCheckInMinutes = attendanceList.stream()
                .filter(a -> a.getTimeIn() != null)
                .mapToInt(a -> a.getTimeIn().toSecondOfDay())
                .average()
                .orElse(0);

        double averageCheckOutMinutes = attendanceList.stream()
                .filter(a -> a.getTimeOut() != null)
                .mapToInt(a -> a.getTimeOut().toSecondOfDay())
                .average()
                .orElse(0);

        LocalTime averageCheckIn = LocalTime.ofSecondOfDay((long) averageCheckInMinutes);
        LocalTime averageCheckOut = LocalTime.ofSecondOfDay((long) averageCheckOutMinutes);

        AttendanceSummary summary = new AttendanceSummary();
        summary.setTotalCount(totalCount);
        summary.setPresentCount(totalCount);
        summary.setLateCount(lateCount);
        summary.setAbsentCount(absentCount);
        summary.setAverageTimeIn(averageCheckIn);
        summary.setAverageTimeOut(averageCheckOut);

        return summary;
    }

    @Override
    public Attendance updateAttendance(Attendance attendance) {
        if (attendance.getAttendanceId() == null) {
            throw new IllegalArgumentException("Attendance ID must not be null for update");
        }
        return attendanceRepository.save(attendance);
    }

    @Override
    public void deleteAttendance(Long attendanceId) {

    }
}
