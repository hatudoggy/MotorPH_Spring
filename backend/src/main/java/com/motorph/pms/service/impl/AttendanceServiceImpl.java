package com.motorph.ems.service.impl;

import com.motorph.ems.dto.AttendanceDTO;
import com.motorph.ems.dto.AttendanceSummaryDTO;
import com.motorph.ems.dto.mapper.AttendanceMapper;
import com.motorph.ems.model.Attendance;
import com.motorph.ems.repository.AttendanceRepository;
import com.motorph.ems.service.AttendanceService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            AttendanceMapper attendanceMapper
    ) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public AttendanceDTO addNewAttendance(Attendance attendance) {
        if (attendanceRepository.existsByEmployee_EmployeeIdAndDate(attendance.getEmployee().getEmployeeId(), attendance.getDate())) {
            throw new IllegalStateException("Attendance for employee " + attendance.getEmployee().getEmployeeId() + " on " + attendance.getDate() + " already exists");
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);

        return attendanceMapper.toDTO(savedAttendance);
    }

    @Override
    public Optional<AttendanceDTO> getAttendanceById(Long attendanceId) {
        return attendanceRepository.findById(attendanceId).map(attendanceMapper::toDTO);
    }

    @Override
    public Optional<AttendanceDTO> getAttendanceByEmployeeIdAndDate(Long employeeId, LocalDate date) {
        return attendanceRepository.findByEmployee_EmployeeIdAndDate(employeeId, date).map(attendanceMapper::toDTO);
    }

    @Override
    public List<AttendanceDTO> getAllAttendances() {
        return attendanceRepository.findAll().stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAllByEmployeeId(Long employeeId) {
        return attendanceRepository.findAllByEmployee_EmployeeId_OrderByDateDesc(employeeId).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAllByDate(LocalDate date) {
        return attendanceRepository.findAllByDate_OrderByDateDesc(date).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAttendancesForDateRange(LocalDate start, LocalDate end) {
        return attendanceRepository.findAllByDateBetweenOrderByDateDesc(start, end).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAttendancesAfterTimeIn(LocalTime timeIn, LocalDate date) {
        return attendanceRepository.findAllByTimeInIsAfterAndDate(timeIn, date).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAttendancesByDateAndEmployeeName(LocalDate date, String name) {
        return attendanceRepository.findByDateAndNameContaining(date, name).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<AttendanceDTO> getAttendancesAfterTimeOut(LocalTime timeOut, LocalDate date) {
        return attendanceRepository.findAllByTimeOutIsAfterAndDate(timeOut, date).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AttendanceDTO updateAttendance(Long employeeId, AttendanceDTO attendance) {
        Attendance existingAttendance = attendanceRepository.findByEmployee_EmployeeIdAndDate(employeeId, attendance.date()).orElseThrow(
                ()-> new EntityNotFoundException("Attendance for employee " + employeeId + " at " + attendance.date() + " not found")
        );

        attendanceMapper.updateEntity(attendance, existingAttendance);

        Attendance updatedAttendance = attendanceRepository.save(existingAttendance);

        return attendanceMapper.toDTO(updatedAttendance);
    }

    @Override
    public AttendanceSummaryDTO getAttendanceSummaryByEmployeeId(Long employeeId) {
        List<Attendance> attendanceList = attendanceRepository.findAllByEmployee_EmployeeId_OrderByDateDesc(employeeId);

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

        return AttendanceSummaryDTO.builder()
                .totalCount(totalCount)
                .presentCount(totalCount - absentCount)
                .lateCount(lateCount)
                .absentCount(absentCount)
                .averageTimeIn(averageCheckIn)
                .averageTimeOut(averageCheckOut)
                .build();
    }

    @Override
    public void deleteAttendanceById(Long attendanceId) {
        if (!attendanceRepository.existsById(attendanceId)) {
            throw new EntityNotFoundException("Attendance with status " + attendanceId + " does not exist");
        }

        attendanceRepository.deleteById(attendanceId);
    }

    @Override
    public List<AttendanceDTO> getAllAttendanceByEmployeeIdAndDateRange(Long employeeId, LocalDate start, LocalDate end) {
        return attendanceRepository.findAllByEmployee_EmployeeId_AndDateBetween(employeeId, start, end)
                .stream().map(attendanceMapper::toDTO).collect(Collectors.toList());
    }
}
