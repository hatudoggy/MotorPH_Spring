package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record LeaveBalanceDTO (
        Long id,
        Long employeeId,
        LeaveTypeDTO leaveType,
        int balance
){}
