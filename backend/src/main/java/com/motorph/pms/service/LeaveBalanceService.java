package com.motorph.pms.service;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveTypeDTO;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceService {

    Optional<LeaveBalanceDTO> getLeaveBalanceById(Long leaveBalanceId);

    LeaveBalanceDTO updateLeaveBalance(Long leaveBalanceId, LeaveBalanceDTO leaveBalance);

    Optional<LeaveTypeDTO> getLeaveTypeById(int leaveTypeId);

    List<LeaveTypeDTO> getAllLeaveTypes();
}
