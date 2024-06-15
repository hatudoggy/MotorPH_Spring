package com.motorph.ems.service.impl;

import com.motorph.ems.model.LeaveBalance;
import com.motorph.ems.repository.LeaveBalanceRepository;
import com.motorph.ems.service.LeaveBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


// TODO: Implement LeaveBalanceService

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceRepository balanceRepository;

    @Autowired
    public LeaveBalanceServiceImpl(LeaveBalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public LeaveBalance addNewLeaveBalance(LeaveBalance leaveBalance) {
        return null;
    }

    @Override
    public List<LeaveBalance> addMultipleLeaveBalances(List<LeaveBalance> leaveBalances) {
        return List.of();
    }

    @Override
    public LeaveBalance getLeaveBalanceById(Long employeeId) {
        return null;
    }

    @Override
    public LeaveBalance getLeaveBalanceByEmployeeIdAndLeaveType(Long employeeId, String leaveType) {
        return null;
    }

    @Override
    public List<LeaveBalance> getAllLeaveBalances() {
        return List.of();
    }

    @Override
    public List<LeaveBalance> getLeaveBalancesByEmployeeId(Long employeeId) {
        return List.of();
    }

    @Override
    public LeaveBalance updateLeaveBalance(LeaveBalance leaveBalance) {
        return null;
    }

    @Override
    public void deleteLeaveBalance(Long employeeId) {

    }
}
