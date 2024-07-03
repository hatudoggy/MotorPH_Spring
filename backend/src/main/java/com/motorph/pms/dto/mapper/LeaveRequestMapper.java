package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.dto.LeaveStatusDTO;
import com.motorph.pms.model.LeaveRequest;
import com.motorph.pms.model.LeaveRequest.LeaveStatus;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestMapper {

    public LeaveRequestDTO toDTO(LeaveRequest entity) {
        return LeaveRequestDTO.builder()
                .leaveRequestId(entity.getLeaveRequestId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .requestDate(entity.getRequestDate())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .daysRequested(entity.getDaysRequested())
                .statusId(entity.getStatus().getLeaveStatusId())
                .reason(entity.getReason())
                .build();
    }

    public LeaveRequest toEntity(LeaveRequestDTO dto) {
        return new LeaveRequest(
                dto.employeeId(),
                dto.requestDate(),
                dto.startDate(),
                dto.endDate(),
                dto.statusId(),
                dto.reason()
        );
    }

    public LeaveStatusDTO toDTO(LeaveStatus entity) {
        return LeaveStatusDTO.builder()
                .id(entity.getLeaveStatusId())
                .status(entity.getStatusName())
                .build();
    }

    public LeaveStatus toEntity(LeaveStatusDTO dto) {
        return new LeaveStatus(dto.status());
    }

    public void updateEntity(LeaveRequestDTO leaveRequestDTO, LeaveRequest entity) {
        if (!leaveRequestDTO.employeeId().equals(entity.getEmployee().getEmployeeId())) {
            throw new IllegalStateException("Employee ID cannot be changed");
        }

        entity.setRequestDate(leaveRequestDTO.requestDate());
        entity.setStartDate(leaveRequestDTO.startDate());
        entity.setEndDate(leaveRequestDTO.endDate());
        entity.setDaysRequested(leaveRequestDTO.daysRequested());
        entity.setStatus(new LeaveStatus(leaveRequestDTO.statusId()));
        entity.setReason(leaveRequestDTO.reason());
    }
}
