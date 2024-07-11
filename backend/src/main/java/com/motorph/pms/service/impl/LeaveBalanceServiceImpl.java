package com.motorph.pms.service.impl;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.dto.LeaveTypeDTO;
import com.motorph.pms.dto.mapper.LeaveBalanceMapper;
import com.motorph.pms.model.LeaveBalance;
import com.motorph.pms.repository.LeaveBalanceRepository;
import com.motorph.pms.repository.LeaveTypeRepository;
import com.motorph.pms.service.LeaveBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = {"leaveBalance", "leaveBalanceList"})
@Slf4j
@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceRepository balanceRepository;
    private final LeaveBalanceMapper leaveMapper;

    @Autowired
    public LeaveBalanceServiceImpl(
            LeaveBalanceRepository balanceRepository, 
            LeaveBalanceMapper leaveMapper) {
        this.balanceRepository = balanceRepository;
        this.leaveMapper = leaveMapper;
    }

    @CachePut(cacheNames = "leaveBalance", key = "#leaveBalance.id")
    @CacheEvict(cacheNames = "leaveBalanceList", allEntries = true)
    @Override
    public LeaveBalanceDTO addLeaveBalance(LeaveBalanceDTO leaveBalance) {
        log.debug("Adding leave balance: {}", leaveBalance);

        if (leaveBalance.id() != null) {
            throw new IllegalArgumentException("Id should be null");
        }

        return leaveMapper.toDTO(balanceRepository.save(leaveMapper.toEntity(leaveBalance)));
    }

    @Cacheable(cacheNames = "leaveBalanceList")
    @Override
    public List<LeaveBalanceDTO> getLeaveBalances() {
        log.debug("Getting all leave balances");

        return leaveMapper.toDTO(balanceRepository.findAll());
    }

    @Cacheable(cacheNames = "leaveBalanceList", key = "#employeeId")
    @Override
    public List<LeaveBalanceDTO> getLeaveBalancesByEmployeeId(Long employeeId) {
        log.debug("Getting leave balances by employee id: {}", employeeId);

        return leaveMapper.toDTO(balanceRepository.findAllByEmployee_EmployeeId(employeeId));
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "leaveBalanceList", key = "#leaveRequest.employee().employeeId()"),
    })
    @Override
    public boolean deductLeaveBalance(LeaveRequestDTO leaveRequest) {
        log.debug("Deducting leave balance: {}", leaveRequest);

        int daysRequested = calculateDuration(leaveRequest.startDate(), leaveRequest.endDate());

        Optional<LeaveBalance> balance = balanceRepository.findAllByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(
                leaveRequest.employee().employeeId(), leaveRequest.leaveType().id());

        if (balance.isEmpty()) {
            log.error("No balance found for employee {} and leave type {}", leaveRequest.employee().employeeId(), leaveRequest.leaveType().id());
            return false;
        }

        LeaveBalance leaveBalance = balance.get();

        if (leaveBalance.getBalance() < daysRequested) {
            log.error("Insufficient balance for employee {} and leave type {}", leaveRequest.employee().employeeId(), leaveRequest.leaveType().id());
            return false;
        }

        log.debug("Deducting {} days from balance for employee {} and leave type {}", daysRequested, leaveRequest.employee().employeeId(), leaveRequest.leaveType().id());

        // Deduct from balance
        leaveBalance.setBalance(leaveBalance.getBalance() - daysRequested);

        balanceRepository.save(leaveBalance);

        return true;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "leaveBalanceList", key = "#leaveRequest.employee().employeeId()"),
    })
    @Override
    public void restoreLeaveBalance(LeaveRequestDTO leaveRequest) {
        log.debug("Restoring leave balance: {}", leaveRequest);

        int daysRequested = calculateDuration(leaveRequest.startDate(), leaveRequest.endDate());

        Optional<LeaveBalance> balance = balanceRepository.findAllByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(
                leaveRequest.employee().employeeId(), leaveRequest.leaveType().id());

        if (balance.isEmpty()) {
            log.error("No balance found for employee {} and leave type {}", leaveRequest.employee().employeeId(), leaveRequest.leaveType().id());
            return;
        }

        LeaveBalance leaveBalance = balance.get();

        //Add to balance
        log.debug("Adding {} days to balance for employee {} and leave type {}", daysRequested, leaveRequest.employee().employeeId(), leaveRequest.leaveType().id());

        leaveBalance.setBalance(leaveBalance.getBalance() + daysRequested);

        balanceRepository.save(leaveBalance);
    }

    @Cacheable(cacheNames = "leaveBalance", key = "#id")
    @Override
    public LeaveBalanceDTO getLeaveBalance(Long id) {
        log.debug("Getting leave balance by id: {}", id);

        return leaveMapper.toDTO(balanceRepository.findById(id).orElse(null));
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "leaveBalance", key = "#leaveBalanceId"),
            @CacheEvict(cacheNames = "leaveBalanceList", allEntries = true)
    })
    @Override
    public LeaveBalanceDTO updateLeaveBalance(Long leaveBalanceId, LeaveBalanceDTO leaveBalance) {
        log.debug("Updating leave balance: {}", leaveBalance);

        LeaveBalance balance = balanceRepository.findById(leaveBalanceId).orElseThrow(
                () -> new IllegalArgumentException("Leave balance with status " + leaveBalance.id() + " does not exist")
        );

        leaveMapper.updateEntity(leaveBalance, balance);

        return leaveMapper.toDTO(balanceRepository.save(balance));
    }

    private int calculateDuration(LocalDate startDate, LocalDate endDate) {
        return (int) startDate.until(endDate, java.time.temporal.ChronoUnit.DAYS) + 1;
    }
}
