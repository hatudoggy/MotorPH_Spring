package com.motorph.pms.service.impl;

import com.motorph.pms.dto.AttendanceDTO;
import com.motorph.pms.dto.mapper.AttendanceMapper;
import com.motorph.pms.model.Attendance;
import com.motorph.pms.repository.AttendanceRepository;
import com.motorph.pms.service.AttendanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = {"attendanceList", "attendance"})
@Slf4j
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

    @CachePut(cacheNames = "attendance", key = "#result.employee.employeeId")
    @CacheEvict(cacheNames = "attendanceList", allEntries = true)
    @Transactional
    @Override
    public AttendanceDTO addNewAttendance(Attendance attendance) {
        log.debug("Adding new attendance: {}", attendance);

        if (attendanceRepository.existsByEmployee_EmployeeIdAndDate(attendance.getEmployee().getEmployeeId(), attendance.getDate())) {
            throw new IllegalStateException("Attendance for employee " + attendance.getEmployee().getEmployeeId() + " on " + attendance.getDate() + " already exists");
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);

        return attendanceMapper.toDTO(savedAttendance);
    }

    @Cacheable(cacheNames = "attendance", key = "#employeeId")
    @Override
    public Optional<AttendanceDTO> getAttendanceByEmployeeIdAndDate(Long employeeId, LocalDate date) {
        log.debug("Getting attendance for employee {} on {}", employeeId, date);

        return attendanceRepository.findByEmployee_EmployeeIdAndDate(employeeId, date).map(attendanceMapper::toDTO);
    }

    @Cacheable(cacheNames = "attendanceList")
    @Override
    public List<AttendanceDTO> getAllAttendances() {
        log.debug("Getting all attendances");

        return attendanceRepository.findAll().stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "attendanceList", key = "#employeeId")
    @Override
    public List<AttendanceDTO> getAllByEmployeeId(Long employeeId) {
        log.debug("Getting all attendances for employee {}", employeeId);

        return attendanceRepository.findAllByEmployee_EmployeeId_OrderByDateDesc(employeeId).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "attendanceList", key = "#date")
    @Override
    public List<AttendanceDTO> getAllByDate(LocalDate date) {
        log.debug("Getting all attendances for date {}", date);

        return attendanceRepository.findAllByDate_OrderByDateDesc(date).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames="attendance", key = "#employeeId"),
            @CacheEvict(cacheNames="attendanceList", allEntries = true)})
    @Transactional
    @Override
    public AttendanceDTO updateAttendance(Long employeeId, AttendanceDTO attendance) {
        log.debug("Updating attendance for employee {} with {}", employeeId, attendance);

        Attendance existingAttendance = attendanceRepository.findByEmployee_EmployeeIdAndDate(employeeId, attendance.date()).orElseThrow(
                ()-> new EntityNotFoundException("Attendance for employee " + employeeId + " at " + attendance.date() + " not found")
        );

        attendanceMapper.updateEntity(attendance, existingAttendance);

        Attendance updatedAttendance = attendanceRepository.save(existingAttendance);

        return attendanceMapper.toDTO(updatedAttendance);
    }

    @Cacheable(cacheNames = "attendanceList", key = "#start + '_' + #end")
    @Override
    public List<AttendanceDTO> getAllAttendanceByEmployeeIdAndDateRange(Long employeeId, LocalDate start, LocalDate end) {
        log.debug("Getting all attendances for employee {} between {} and {}", employeeId, start, end);

        return attendanceRepository.findAllByEmployee_EmployeeId_AndDateBetween(employeeId, start, end).stream()
                .map(attendanceMapper::toDTO).collect(Collectors.toList());
    }
}
