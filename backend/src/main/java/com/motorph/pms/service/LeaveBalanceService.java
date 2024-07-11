package com.motorph.pms.service;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.dto.LeaveTypeDTO;

import java.util.List;

public interface LeaveBalanceService {
    LeaveBalanceDTO addLeaveBalance(LeaveBalanceDTO leaveBalance);

    List<LeaveBalanceDTO> getLeaveBalances();

    LeaveBalanceDTO getLeaveBalance(Long id);

    LeaveBalanceDTO updateLeaveBalance(Long leaveBalanceId, LeaveBalanceDTO leaveBalance);

    List<LeaveBalanceDTO> getLeaveBalancesByEmployeeId(Long employeeId);

    boolean deductLeaveBalance(LeaveRequestDTO leaveRequest);

    void restoreLeaveBalance(LeaveRequestDTO leaveRequestDTO);
}
