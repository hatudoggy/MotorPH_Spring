package com.motorph.pms.service.impl;

import com.motorph.pms.dto.LeaveRequestDTO;

import com.motorph.pms.dto.mapper.LeaveRequestMapper;
import com.motorph.pms.model.LeaveBalance;
import com.motorph.pms.model.LeaveRequest;
import com.motorph.pms.repository.LeaveBalanceRepository;
import com.motorph.pms.repository.LeaveRequestRepository;
import com.motorph.pms.service.LeaveBalanceService;
import com.motorph.pms.service.LeaveRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = {"leaveRequest", "leaveRequestList"})
@Slf4j
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository requestRepository;
    private final LeaveBalanceService balanceService;
    private final LeaveRequestMapper leaveRequestMapper;

    @Autowired
    public LeaveRequestServiceImpl(LeaveRequestRepository requestRepository,
                                   LeaveBalanceService balanceService,
                                   LeaveRequestMapper leaveRequestMapper) {
        this.requestRepository = requestRepository;
        this.balanceService = balanceService;
        this.leaveRequestMapper = leaveRequestMapper;
    }

    @CachePut(cacheNames = "leaveRequest", key = "#result.leaveRequestId")
    @CacheEvict(cacheNames = "leaveRequestList", allEntries = true)
    @Override
    public LeaveRequestDTO addNewLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        log.debug("Adding new leave request: {}", leaveRequestDTO);

        if (requestRepository.existsByEmployee_EmployeeIdAndStartDateAfterAndEndDateBefore(
                leaveRequestDTO.employee().employeeId(),
                leaveRequestDTO.startDate(),
                leaveRequestDTO.endDate())) {
            throw new IllegalStateException("Leave request has conflicting dates");
        }

        if (!balanceService.deductLeaveBalance(leaveRequestDTO)) {
            throw new IllegalStateException("Insufficient leave balance");
        }

        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(leaveRequestDTO);

        return leaveRequestMapper.toDTO(requestRepository.save(leaveRequest));
    }

    @Cacheable(cacheNames = "leaveRequestList")
    @Override
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        log.debug("Fetching all leave requests");

        return requestRepository.findAll().stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "leaveRequestList", key = "#employeeId")
    @Override
    public List<LeaveRequestDTO> getLeaveRequestsByEmployeeId(Long employeeId) {
        log.debug("Fetching leave requests by employee ID: {}", employeeId);

        return requestRepository.findAllByEmployee_EmployeeId(employeeId).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "leaveRequest", key = "#result.leaveRequestId"),
            @CacheEvict(cacheNames = "leaveRequestList", allEntries = true)
    })
    @Override
    public LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        log.debug("Updating leave request with Id: {}", leaveRequestDTO.leaveRequestId());

        LeaveRequest existingRequest = requestRepository.findById(leaveRequestDTO.leaveRequestId()).orElseThrow(
                () -> new IllegalStateException("Leave request does not exist")
        );

        if (leaveRequestDTO.status().id() == 3 && existingRequest.getStatus().getLeaveStatusId() != 3) {
            balanceService.restoreLeaveBalance(leaveRequestDTO);
        }

        leaveRequestMapper.updateEntity(leaveRequestDTO, existingRequest);

        return leaveRequestMapper.toDTO(requestRepository.save(existingRequest));
    }


    @Caching(evict = {
            @CacheEvict(cacheNames = "leaveRequest", key = "#result.leaveRequestId()"),
            @CacheEvict(cacheNames = "leaveRequestList", allEntries = true)
    })
    public void deleteLeaveRequest(Long leaveRequestId) {
        log.debug("Deleting leave request with Id: {}", leaveRequestId);

        if (!requestRepository.existsById(leaveRequestId)) {
            throw new IllegalStateException("Leave request does not exist");
        }

        requestRepository.deleteById(leaveRequestId);
    }

    @Cacheable(cacheNames = "leaveRequestList", key = "#after + '_' + #before")
    @Override
    public List<LeaveRequestDTO> getLeaveRequestsByRequestDateRange(LocalDate after, LocalDate before) {
        log.debug("Fetching leave requests by date range: {} - {}", after, before);

        return requestRepository.findAllByRequestDateAfterAndRequestDateBefore(after, before).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }
}
