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
    public List<LeaveRequestDTO> getAllLeaveRequestsByEmployeeId(Long employeeId) {
        return requestRepository.findAllByEmployee_EmployeeId(employeeId).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequestsByPositionCode(String positionCode) {
        return requestRepository.findAllByEmployee_Position_PositionCode(positionCode).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequestsByDepartmentCode(String departmentCode) {
        return requestRepository.findAllByEmployee_Department_DepartmentCode(departmentCode).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequestsByStatus(String status) {
        return requestRepository.findAllByStatus_StatusName(status).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequestsByRequestDate(LocalDate requestDate) {
        return requestRepository.findAllByRequestDate(requestDate).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequestsByStartDate(LocalDate startDate) {
        return requestRepository.findAllByStartDate(startDate).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequestsByRequestDateBetween(LocalDate startDate, LocalDate endDate) {
        return requestRepository.findAllByRequestDateBetween(startDate, endDate).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequestsByStatusAndRequestDateBetween(String status, LocalDate startDate, LocalDate endDate) {
        return requestRepository.findAllByStatus_StatusNameAndRequestDateBetween(status, startDate, endDate).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequestsBySupervisorId(Long supervisorId) {
        return requestRepository.findAllByEmployee_Supervisor_EmployeeId(supervisorId).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<LeaveRequestDTO> getLeaveRequestById(Long leaveRequestId) {
        return requestRepository.findById(leaveRequestId).map(leaveRequestMapper::toDTO);
    }

    @Override
    public Optional<LeaveRequestDTO> getLeaveRequestByEmployeeIdAndDate(Long employeeId, LocalDate date) {
        return requestRepository.findByEmployee_EmployeeIdAndRequestDate(employeeId, date).map(leaveRequestMapper::toDTO);
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

    @Cacheable(value = "leaveStatuses", key = "#leaveStatusId")
    @Override
    public Optional<LeaveStatusDTO> getLeaveStatusById(int leaveStatusId) {
        return statusRepository.findById(leaveStatusId).map(leaveRequestMapper::toDTO);
    }

//    @Override
//    public Optional<LeaveStatusDTO> getLeaveStatusByStatusName(String statusName) {
//        return statusRepository.findByStatusName(statusName).map(leaveRequestMapper::toDTO);
//    }

    @Cacheable("leaveStatuses")
    @Override
    public List<LeaveStatusDTO> getAllLeaveStatus() {
        return statusRepository.findAll().stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }
}
