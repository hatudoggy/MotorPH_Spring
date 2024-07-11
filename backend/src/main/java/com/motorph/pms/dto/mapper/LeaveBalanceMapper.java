package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveTypeDTO;
import com.motorph.pms.model.LeaveBalance;
import com.motorph.pms.model.LeaveBalance.LeaveType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LeaveBalanceMapper {

    public LeaveBalanceDTO toDTO(LeaveBalance leaveBalance) {
        if (leaveBalance == null) {
            return null;
        }

        return LeaveBalanceDTO.builder()
                .id(leaveBalance.getLeaveBalanceId())
                .employeeId(leaveBalance.getEmployee().getEmployeeId())
                .leaveType(toDTO(leaveBalance.getLeaveType()))
                .balance(leaveBalance.getBalance())
                .build();
    }

    public List<LeaveBalanceDTO> toDTO(List<LeaveBalance> leaveBalances) {
        if (leaveBalances == null) {
            return null;
        }

        return leaveBalances.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LeaveBalance toEntity(LeaveBalanceDTO leaveBalanceDTO) {
        if (leaveBalanceDTO == null || leaveBalanceDTO.employeeId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when creating leave balance");
        }

        return new LeaveBalance(
                leaveBalanceDTO.employeeId(),
                leaveBalanceDTO.leaveType().id(),
                leaveBalanceDTO.balance()
        );
    }

    public List<LeaveBalance> toEntity(List<LeaveBalanceDTO> leaveBalanceDTOS) {
        if (leaveBalanceDTOS == null) {
            return null;
        }

        return leaveBalanceDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public void updateEntity(LeaveBalanceDTO leaveBalanceDTO, LeaveBalance leaveBalance) {
        if (leaveBalanceDTO == null || leaveBalanceDTO.employeeId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when updating leave balance");
        }

        if (!leaveBalance.getEmployee().getEmployeeId().equals(leaveBalanceDTO.employeeId())) {
            throw new IllegalArgumentException("Employee ID does not match when updating leave balance");
        }

        leaveBalance.setBalance(leaveBalanceDTO.balance());
    }

    public LeaveTypeDTO toDTO(LeaveType leaveType) {
        if (leaveType == null) {
            return null;
        }

        return LeaveTypeDTO.builder()
                .id(leaveType.getLeaveTypeId())
                .typeName(leaveType.getType())
                .build();
    }
}
