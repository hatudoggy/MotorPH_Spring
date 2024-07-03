package com.motorph.pms.service.impl;

import com.motorph.pms.dto.AttendanceDTO;
import com.motorph.pms.dto.AttendanceSummaryDTO;
import com.motorph.pms.dto.mapper.AttendanceMapper;
import com.motorph.pms.model.Attendance;
import com.motorph.pms.repository.AttendanceRepository;
import com.motorph.pms.service.AttendanceService;
import jakarta.persistence.EntityNotFoundException;
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
    public List<AttendanceDTO> getAllAttendanceByEmployeeIdAndDateRange(Long id, LocalDate start, LocalDate end) {
        return attendanceRepository.findAllByEmployee_EmployeeId_AndDateBetween(id, start, end).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }
}
