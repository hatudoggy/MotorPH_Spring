package com.motorph.ems.service;

import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.LeaveRequest.LeaveStatus;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestService {

    LeaveRequest addNewLeaveRequest(LeaveRequest leaveRequest);

    List<LeaveRequest> getAllLeaveRequests();

    List<LeaveRequest> getAllLeaveRequestsByEmployeeId(Long employeeId);

    List<LeaveRequest> getAllLeaveRequestsByStatus(LeaveStatus status);

    LeaveRequest getLeaveRequestById(Long leaveRequestId);

    LeaveRequest getLeaveRequestByEmployeeIdAndDate(Long employeeId, LocalDate date);

    LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest);

    void deleteLeaveRequest(Long leaveRequestId);
}
