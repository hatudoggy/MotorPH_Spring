package com.motorph.pms.service.impl;

import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.dto.LeaveStatusDTO;
import com.motorph.pms.dto.mapper.LeaveRequestMapper;
import com.motorph.pms.model.LeaveRequest;
import com.motorph.pms.repository.LeaveRequestRepository;
import com.motorph.pms.repository.LeaveStatusRepository;
import com.motorph.pms.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository requestRepository;
    private final LeaveStatusRepository statusRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    @Autowired
    public LeaveRequestServiceImpl(LeaveRequestRepository requestRepository,
                                   LeaveStatusRepository statusRepository,
                                   LeaveRequestMapper leaveRequestMapper) {
        this.requestRepository = requestRepository;
        this.statusRepository = statusRepository;
        this.leaveRequestMapper = leaveRequestMapper;
    }

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

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        return requestRepository.findAll().stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LeaveRequestDTO> getLeaveRequestById(Long leaveRequestId) {
        return requestRepository.findById(leaveRequestId).map(leaveRequestMapper::toDTO);
    }

    @Override
    public LeaveRequestDTO updateLeaveRequest(Long leaveRequestId, LeaveRequestDTO leaveRequestDTO) {
        LeaveRequest existingRequest = requestRepository.findById(leaveRequestId).orElseThrow(
                () -> new IllegalStateException("Leave request does not exist")
        );

        leaveRequestMapper.updateEntity(leaveRequestDTO, existingRequest);

        return leaveRequestMapper.toDTO(requestRepository.save(existingRequest));
    }


    @Override
    public void deleteLeaveRequest(Long leaveRequestId) {
        if (!requestRepository.existsById(leaveRequestId)) {
            throw new IllegalStateException("Leave request does not exist");
        }

        requestRepository.deleteById(leaveRequestId);
    }

    @Override
    public Optional<LeaveStatusDTO> getLeaveStatusById(int leaveStatusId) {
        return statusRepository.findById(leaveStatusId).map(leaveRequestMapper::toDTO);
    }

    @Override
    public List<LeaveStatusDTO> getAllLeaveStatus() {
        return statusRepository.findAll().stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }
}
