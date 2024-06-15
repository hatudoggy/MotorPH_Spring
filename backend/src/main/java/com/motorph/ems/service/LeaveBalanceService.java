package com.motorph.ems.service;

import com.motorph.ems.model.LeaveBalance;

import java.util.List;

public interface LeaveBalanceService {

    LeaveBalance addNewLeaveBalance(LeaveBalance leaveBalance);

    List<LeaveBalance> addMultipleLeaveBalances(List<LeaveBalance> leaveBalances);

    LeaveBalance getLeaveBalanceById(Long employeeId);

    LeaveBalance getLeaveBalanceByEmployeeIdAndLeaveType(Long employeeId, String leaveType);

    List<LeaveBalance> getAllLeaveBalances();

    List<LeaveBalance> getLeaveBalancesByEmployeeId(Long employeeId);

    LeaveBalance updateLeaveBalance(LeaveBalance leaveBalance);

    void deleteLeaveBalance(Long employeeId);
}
