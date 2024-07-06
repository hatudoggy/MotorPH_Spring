package com.motorph.pms.service.impl;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveTypeDTO;
import com.motorph.pms.dto.mapper.LeaveBalanceMapper;
import com.motorph.pms.model.LeaveBalance;
import com.motorph.pms.repository.LeaveBalanceRepository;
import com.motorph.pms.repository.LeaveTypeRepository;
import com.motorph.pms.service.LeaveBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceRepository balanceRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceMapper leaveMapper;

    @Autowired
    public LeaveBalanceServiceImpl(
            LeaveBalanceRepository balanceRepository, 
            LeaveTypeRepository leaveTypeRepository, 
            LeaveBalanceMapper leaveMapper) {
        this.balanceRepository = balanceRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveMapper = leaveMapper;
    }

    @Override
    public LeaveBalanceDTO updateLeaveBalance(Long leaveBalanceId, LeaveBalanceDTO leaveBalance) {
        LeaveBalance balance = balanceRepository.findById(leaveBalanceId).orElseThrow(
                () -> new IllegalArgumentException("Leave balance with status " + leaveBalance.id() + " does not exist")
        );

        leaveMapper.updateEntity(leaveBalance, balance);

        return leaveMapper.toDTO(balanceRepository.save(balance));
    }


}
