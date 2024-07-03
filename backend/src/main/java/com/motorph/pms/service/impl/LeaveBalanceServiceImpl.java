package com.motorph.ems.service.impl;

import com.motorph.ems.dto.LeaveBalanceDTO;
import com.motorph.ems.dto.LeaveTypeDTO;
import com.motorph.ems.dto.mapper.LeaveBalanceMapper;
import com.motorph.ems.model.LeaveBalance;
import com.motorph.ems.repository.LeaveBalanceRepository;
import com.motorph.ems.repository.LeaveTypeRepository;
import com.motorph.ems.service.LeaveBalanceService;
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
    public LeaveBalanceDTO addNewLeaveBalance(LeaveBalanceDTO leaveBalance) {
        if (balanceRepository.existsByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(
                leaveBalance.employeeId(), leaveBalance.leaveTypeId()))
        {
            throw new IllegalArgumentException(
                    "Leave balance already exists for this employee and leave type");
        }

        return leaveMapper.toDTO(balanceRepository.save(leaveMapper.toEntity(leaveBalance)));
    }

    @Override
    public List<LeaveBalanceDTO> addMultipleLeaveBalances(List<LeaveBalanceDTO> leaveBalances) {
        if (leaveBalances.isEmpty()) {
            throw new IllegalArgumentException("No new leave balance to add");
        }

        List<LeaveBalance> currentBalances = balanceRepository.findAllByEmployee_EmployeeId(leaveBalances.getFirst().employeeId());

        if (!currentBalances.isEmpty()){
            //Remove any leave balances that already exist
            leaveBalances.removeIf(balance -> currentBalances.stream()
                    .anyMatch(leaveBalance -> leaveBalance.getLeaveType().getLeaveTypeId() == balance.id()));
        }

        if (leaveBalances.isEmpty()) {
            throw new IllegalArgumentException("Leave balances already exist for this employee");
        }

        List<LeaveBalance> savedBalances = balanceRepository.saveAll(leaveBalances.stream()
                .map(leaveMapper::toEntity).toList());

        return savedBalances.stream().map(leaveMapper::toDTO).toList();
    }

    @Override
    public Optional<LeaveBalanceDTO> getLeaveBalanceById(Long leaveBalanceId) {
        return balanceRepository.findById(leaveBalanceId).map(leaveMapper::toDTO);
    }

    @Override
    public Optional<LeaveBalanceDTO> getLeaveBalanceByEmployeeIdAndLeaveType(Long employeeId, int leaveTypeId) {
        return balanceRepository.getLeaveBalanceByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(employeeId, leaveTypeId)
                .map(leaveMapper::toDTO);
    }

    @Override
    public List<LeaveBalanceDTO> getAllLeaveBalances() {
        return balanceRepository.findAll().stream()
                .map(leaveMapper::toDTO).toList();
    }

    @Override
    public List<LeaveBalanceDTO> getLeaveBalancesByEmployeeId(Long employeeId) {
        return balanceRepository.findAllByEmployee_EmployeeId(employeeId).stream()
                .map(leaveMapper::toDTO).toList();
    }

    @Override
    public LeaveBalanceDTO updateLeaveBalance(Long leaveBalanceId, LeaveBalanceDTO leaveBalance) {
        LeaveBalance balance = balanceRepository.findById(leaveBalanceId).orElseThrow(
                () -> new IllegalArgumentException("Leave balance with status " + leaveBalance.id() + " does not exist")
        );

        leaveMapper.updateEntity(leaveBalance, balance);

        return leaveMapper.toDTO(balanceRepository.save(balance));
    }

    @Override
    public void deleteLeaveBalanceById(Long employeeId) {
        balanceRepository.deleteById(employeeId);
    }

    @Override
    public void deleteMultipleLeaveBalancesByEmployeeId(Long employeeId) {
        List<Long> ids = new ArrayList<>();

        balanceRepository.findAllByEmployee_EmployeeId(employeeId).forEach(leaveBalance -> ids.add(leaveBalance.getLeaveBalanceId()));

        balanceRepository.deleteAllById(ids);
    }

    @Override
    public LeaveTypeDTO addNewLeaveType(LeaveTypeDTO leaveType) {
        if (leaveTypeRepository.existsByType(leaveType.typeName())) {
            throw new IllegalArgumentException("Leave type already exists");
        }

        return leaveMapper.toLeaveTypeDTO(leaveTypeRepository.save(leaveMapper.toLeaveTypeEntity(leaveType)));
    }

    @Cacheable(value = "leaveTypes", key = "#leaveTypeId")
    @Override
    public Optional<LeaveTypeDTO> getLeaveTypeById(int leaveTypeId) {
        return leaveTypeRepository.findById(leaveTypeId).map(leaveMapper::toLeaveTypeDTO);
    }

//    @Override
//    public Optional<LeaveTypeDTO> getLeaveTypeByTypeName(String leaveTypeName) {
//        return leaveTypeRepository.findByType(leaveTypeName).map(leaveMapper::toLeaveTypeDTO);
//    }

    @Cacheable("leaveTypes")
    @Override
    public List<LeaveTypeDTO> getAllLeaveTypes() {
        return leaveTypeRepository.findAll().stream().map(leaveMapper::toLeaveTypeDTO).toList();
    }
}
