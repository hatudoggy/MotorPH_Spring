package com.motorph.ems.service.impl;

import com.motorph.ems.dto.AttendanceDTO;
import com.motorph.ems.dto.mapper.AttendanceMapper;
import com.motorph.ems.model.Attendance;
import com.motorph.ems.repository.AttendanceRepository;
import com.motorph.ems.service.AttendanceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public AttendanceDTO addNewAttendance(AttendanceDTO attendanceDTO) {
        if (attendanceRepository.existsByEmployee_EmployeeIdAndDate(attendanceDTO.employeeId(), attendanceDTO.date())) {
            throw new IllegalStateException("Attendance with employee statusId " + attendanceDTO.employeeId() + " on " + attendanceDTO.date() + " already exists");
        }

        Attendance savedAttendance = attendanceRepository.save(attendanceMapper.toEntity(attendanceDTO));

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
    public List<AttendanceDTO> getAllAttendancesByEmployeeId(Long employeeId) {
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
    public List<AttendanceDTO> getAttendancesAfterTimeOut(LocalTime timeOut, LocalDate date) {
        return attendanceRepository.findAllByTimeOutIsAfterAndDate(timeOut, date).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }
    @Override
    public AttendanceDTO updateAttendance(Long attendanceId, AttendanceDTO attendanceDTO) {
        Attendance existingAttendance = attendanceRepository.findById(attendanceId).orElseThrow(
                () -> new EntityNotFoundException("Attendance with statusId " + attendanceId + " does not exist"));

        // Update existingAttendance with data from attendanceDTO
        attendanceMapper.updateFromDTO(attendanceDTO, existingAttendance);

        // Save the updated entity back to the repository
        return attendanceMapper.toDTO(attendanceRepository.save(existingAttendance));
    }

    @Override
    public void deleteAttendanceById(Long attendanceId) {
        if (!attendanceRepository.existsById(attendanceId)) {

            throw new EntityNotFoundException("Attendance with statusId " + attendanceId + " does not exist");
        }

        attendanceRepository.deleteById(attendanceId);
    }
}
