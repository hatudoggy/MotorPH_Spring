package com.motorph.ems.dto.mapper;

import com.motorph.ems.dto.AttendanceDTO;
import com.motorph.ems.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttendanceMapper {

    private final EmployeeMapper employeeMapper;

    @Autowired
    public AttendanceMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public AttendanceDTO toDTO(Attendance entity) {
        if (entity == null || entity.getEmployee() == null) {
            return null;
        }

        return AttendanceDTO.builder()
                .attendanceId(entity.getAttendanceId())
                .date(entity.getDate())
                .timeIn(entity.getTimeIn())
                .timeOut(entity.getTimeOut() == null ? null : entity.getTimeOut())
                .totalHours(entity.getTimeOut() == null ? 0 : entity.getTotalHours() )
                .overtimeHours(entity.getTimeOut() == null ? 0 : entity.getOvertimeHours() )
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
        if (dto == null || dto.employee() == null) {
            return null;
        }

        return new Attendance(
                dto.employee().employeeId(),
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

    public void updateEntity(AttendanceDTO dto, Attendance entity) {
        if (dto.employee() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when updating attendance");
        }

        if (!dto.employee().equals(entity.getEmployee().getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID does not match when updating attendance");
        }

        entity.setTimeOut(dto.timeOut());
        entity.setTotalHours(calculateTotalHours(entity));
        entity.setOvertimeHours(calculateOvertimeHours(entity));
    }

    private double calculateTotalHours(Attendance entity) {
        if (entity.getTimeIn() == null || entity.getTimeOut() == null) {
            return 0;
        }
        // Calculate total hours based on your logic
        return entity.calculateTotalHours();
    }

    private double calculateOvertimeHours(Attendance entity) {
        if (entity.getTimeIn() == null || entity.getTimeOut() == null) {
            return 0;
        }
        // Calculate overtime hours based on your logic
        return entity.calculateOvertime();
    }
}
