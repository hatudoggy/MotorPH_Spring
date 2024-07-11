package com.motorph.pms.service;

import com.motorph.pms.dto.LeaveRequestDTO;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestService {

    LeaveRequestDTO addNewLeaveRequest(LeaveRequestDTO leaveRequest);

    List<LeaveRequestDTO> getAllLeaveRequests();

    List<LeaveRequestDTO> getLeaveRequestsByEmployeeId(Long employeeId);

    LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequest);

    void deleteLeaveRequest(Long leaveRequestId);

    List<LeaveRequestDTO> getLeaveRequestsByRequestDateRange(LocalDate after, LocalDate before);
}

