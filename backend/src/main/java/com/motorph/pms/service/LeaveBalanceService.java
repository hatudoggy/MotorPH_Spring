package com.motorph.pms.service;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveTypeDTO;

import java.util.List;

public interface LeaveBalanceService {
    LeaveBalanceDTO updateLeaveBalance(Long leaveBalanceId, LeaveBalanceDTO leaveBalance);
}
