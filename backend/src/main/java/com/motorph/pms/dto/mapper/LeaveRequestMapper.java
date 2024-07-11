package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.dto.LeaveStatusDTO;
import com.motorph.pms.model.LeaveBalance;
import com.motorph.pms.model.LeaveRequest;
import com.motorph.pms.model.LeaveRequest.LeaveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestMapper {

    private final LeaveBalanceMapper leaveBalanceMapper;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public LeaveRequestMapper(LeaveBalanceMapper leaveBalanceMapper,
                              EmployeeMapper employeeMapper) {
        this.leaveBalanceMapper = leaveBalanceMapper;
        this.employeeMapper = employeeMapper;
    }

    public LeaveRequestDTO toDTO(LeaveRequest entity) {
        return LeaveRequestDTO.builder()
                .leaveRequestId(entity.getLeaveRequestId())
                .employee(employeeMapper.toLimitedDTO(entity.getEmployee()))
                .leaveType(leaveBalanceMapper.toDTO(entity.getLeaveType()))
                .requestDate(entity.getRequestDate())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .daysRequested(entity.getDaysRequested())
                .status(toDTO(entity.getStatus()))
                .reason(entity.getReason())
                .build();
    }

    public LeaveRequest toEntity(LeaveRequestDTO dto) {
        return new LeaveRequest(
                dto.employee().employeeId(),
                dto.leaveType().id(),
                dto.requestDate(),
                dto.startDate(),
                dto.endDate(),
                dto.status().id(),
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
        if (!leaveRequestDTO.employee().employeeId().equals(entity.getEmployee().getEmployeeId())) {
            throw new IllegalStateException("Employee ID cannot be changed");
        }

        entity.setLeaveType(new LeaveBalance.LeaveType(leaveRequestDTO.leaveType().id()));
        entity.setRequestDate(leaveRequestDTO.requestDate());
        entity.setStartDate(leaveRequestDTO.startDate());
        entity.setEndDate(leaveRequestDTO.endDate());
        entity.setDaysRequested(leaveRequestDTO.daysRequested());
        entity.setStatus(new LeaveStatus(leaveRequestDTO.status().id()));
        entity.setReason(leaveRequestDTO.reason());
    }
}
