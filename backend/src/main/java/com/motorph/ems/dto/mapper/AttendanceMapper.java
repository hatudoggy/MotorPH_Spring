package com.motorph.ems.dto.mapper;

import com.motorph.ems.dto.AttendanceDTO;
import com.motorph.ems.model.Attendance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttendanceMapper {

    public AttendanceDTO toDTO(Attendance entity) {
        if (entity == null || entity.getEmployee() == null) {
            return null;
        }

        return AttendanceDTO.builder()
                .attendanceId(entity.getAttendanceId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .date(entity.getDate())
                .timeIn(entity.getTimeIn())
                .timeOut(entity.getTimeOut() == null ? null : entity.getTimeOut())
                .totalHours(entity.getTimeOut() == null ? 0 : calculateTotalHours(entity) )
                .overtimeHours(entity.getTimeOut() == null ? 0 : calculateOvertimeHours(entity) )
                .build();
    }

    public List<AttendanceDTO> toDTO(List<Attendance> entity) {
        if (entity == null) {
            return null;
        }

        return entity.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Attendance toEntity(AttendanceDTO dto) {
        if (dto == null || dto.employeeId() == null) {
            return null;
        }

        return new Attendance(
                dto.employeeId(),
                dto.date(),
                dto.timeIn(),
                dto.timeOut()
        );
    }

    public List<Attendance> toEntity(List<AttendanceDTO> dto) {
        if (dto == null) {
            return null;
        }

        return dto.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public void updateFromDTO(AttendanceDTO dto, Attendance entity) {
        if (dto.employeeId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when updating attendance");
        }

        if (!dto.employeeId().equals(entity.getEmployee().getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID does not match when updating attendance");
        }

        entity.setTimeOut(dto.timeOut());
    }

    private double calculateTotalHours(Attendance entity) {
        if (entity.getTimeIn() == null || entity.getTimeOut() == null) {
            return 0;
        }
        // Calculate total hours based on your logic
        return entity.getTotalHours();
    }

    private double calculateOvertimeHours(Attendance entity) {
        if (entity.getTimeIn() == null || entity.getTimeOut() == null) {
            return 0;
        }
        // Calculate overtime hours based on your logic
        return entity.getOvertimeHours();
    }
}
