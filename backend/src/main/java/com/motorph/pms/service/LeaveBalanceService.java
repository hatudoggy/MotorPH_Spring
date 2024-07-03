package com.motorph.pms.service;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveTypeDTO;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceService {

    LeaveBalanceDTO addNewLeaveBalance(LeaveBalanceDTO leaveBalance);

    List<LeaveBalanceDTO> addMultipleLeaveBalances(List<LeaveBalanceDTO> leaveBalances);

    Optional<LeaveBalanceDTO> getLeaveBalanceById(Long leaveBalanceId);

    Optional<LeaveBalanceDTO> getLeaveBalanceByEmployeeIdAndLeaveType(Long employeeId, int leaveTypeId);

    List<LeaveBalanceDTO> getAllLeaveBalances();

    List<LeaveBalanceDTO> getLeaveBalancesByEmployeeId(Long employeeId);

    LeaveBalanceDTO updateLeaveBalance(Long leaveBalanceId, LeaveBalanceDTO leaveBalance);

    void deleteLeaveBalanceById(Long leaveBalanceId);

    void deleteMultipleLeaveBalancesByEmployeeId(Long employeeId);

    LeaveTypeDTO addNewLeaveType(LeaveTypeDTO leaveType);

    Optional<LeaveTypeDTO> getLeaveTypeById(int leaveTypeId);

//    Optional<LeaveTypeDTO> getLeaveTypeByTypeName(String leaveTypeName);

    List<LeaveTypeDTO> getAllLeaveTypes();
}
