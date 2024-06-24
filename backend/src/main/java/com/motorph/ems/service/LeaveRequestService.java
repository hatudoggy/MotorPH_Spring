package com.motorph.ems.service;

import com.motorph.ems.dto.LeaveRequestDTO;
import com.motorph.ems.dto.LeaveStatusDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestService {

    LeaveRequestDTO addNewLeaveRequest(LeaveRequestDTO leaveRequest);

    List<LeaveRequestDTO> getAllLeaveRequests();

    List<LeaveRequestDTO> getAllLeaveRequestsByEmployeeId(Long employeeId);

    List<LeaveRequestDTO> getAllLeaveRequestsByPositionCode(String positionCode);

    List<LeaveRequestDTO> getAllLeaveRequestsByDepartmentCode(String departmentCode);

    List<LeaveRequestDTO> getAllLeaveRequestsByStatus(String status);

    List<LeaveRequestDTO> getAllLeaveRequestsByRequestDate(LocalDate requestDate);

    List<LeaveRequestDTO> getAllLeaveRequestsByStartDate(LocalDate startDate);

    List<LeaveRequestDTO> getAllLeaveRequestsByRequestDateBetween(LocalDate startDate, LocalDate endDate);

    List<LeaveRequestDTO> getAllLeaveRequestsByStatusAndRequestDateBetween(String status, LocalDate startDate, LocalDate endDate);

    List<LeaveRequestDTO> getAllLeaveRequestsBySupervisorId(Long supervisorId);

    Optional<LeaveRequestDTO> getLeaveRequestById(Long leaveRequestId);

    Optional<LeaveRequestDTO> getLeaveRequestByEmployeeIdAndDate(Long employeeId, LocalDate date);

    LeaveRequestDTO updateLeaveRequest(Long id, LeaveRequestDTO leaveRequest);

    void deleteLeaveRequest(Long leaveRequestId);

    Optional<LeaveStatusDTO> getLeaveStatusById(int leaveStatusId);

    Optional<LeaveStatusDTO> getLeaveStatusByStatusName(String statusName);

    List<LeaveStatusDTO> getAllLeaveStatus();
}

