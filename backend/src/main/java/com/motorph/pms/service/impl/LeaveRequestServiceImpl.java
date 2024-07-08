package com.motorph.pms.service.impl;

import com.motorph.pms.dto.LeaveRequestDTO;

import com.motorph.pms.dto.mapper.LeaveRequestMapper;
import com.motorph.pms.model.LeaveRequest;
import com.motorph.pms.repository.LeaveRequestRepository;
import com.motorph.pms.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "leaveRequests")
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository requestRepository;
    private final LeaveRequestMapper leaveRequestMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public LeaveRequestServiceImpl(LeaveRequestRepository requestRepository,
                                   LeaveRequestMapper leaveRequestMapper,
                                   ApplicationEventPublisher eventPublisher) {
        this.requestRepository = requestRepository;
        this.leaveRequestMapper = leaveRequestMapper;
        this.eventPublisher = eventPublisher;
    }

    @CachePut(key = "#result.leaveRequestId()")
    @Override
    public LeaveRequestDTO addNewLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        if (requestRepository.existsByEmployeeIdAndDateRange(
                leaveRequestDTO.employeeId(),
                leaveRequestDTO.startDate(),
                leaveRequestDTO.endDate())) {
            throw new IllegalStateException("Leave request has conflicting dates");
        }

        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(leaveRequestDTO);

        return leaveRequestMapper.toDTO(requestRepository.save(leaveRequest));
    }

    @Cacheable()
    @Override
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        return requestRepository.findAll().stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#employeeId")
    @Override
    public List<LeaveRequestDTO> getLeaveRequestsByEmployeeId(Long employeeId) {
        return requestRepository.findAllByEmployee_EmployeeId(employeeId).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @CachePut(key = "#result.leaveRequestId()")
    @Override
    public LeaveRequestDTO updateLeaveRequest(Long leaveRequestId, LeaveRequestDTO leaveRequestDTO) {
        LeaveRequest existingRequest = requestRepository.findById(leaveRequestId).orElseThrow(
                () -> new IllegalStateException("Leave request does not exist")
        );

        leaveRequestMapper.updateEntity(leaveRequestDTO, existingRequest);

        return leaveRequestMapper.toDTO(requestRepository.save(existingRequest));
    }


    @CacheEvict(key = "#leaveRequestId")
    @Override
    public void deleteLeaveRequest(Long leaveRequestId) {
        if (!requestRepository.existsById(leaveRequestId)) {
            throw new IllegalStateException("Leave request does not exist");
        }

        requestRepository.deleteById(leaveRequestId);
    }
}
