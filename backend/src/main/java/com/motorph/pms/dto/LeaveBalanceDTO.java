package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record LeaveBalanceDTO (
        Long id,
        Long employeeId,
        int leaveTypeId,
        int balance
){}
