package com.motorph.pms.service;

import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.dto.LeaveStatusDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestService {

    LeaveRequestDTO addNewLeaveRequest(LeaveRequestDTO leaveRequest);

    List<LeaveRequestDTO> getAllLeaveRequests();

    List<LeaveRequestDTO> getLeaveRequestsByEmployeeId(Long employeeId);

    LeaveRequestDTO updateLeaveRequest(Long id, LeaveRequestDTO leaveRequest);

    void deleteLeaveRequest(Long leaveRequestId);
}

