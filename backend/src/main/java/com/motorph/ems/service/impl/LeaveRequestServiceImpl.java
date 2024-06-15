package com.motorph.ems.service.impl;

import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.repository.LeaveRequestRepository;
import com.motorph.ems.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

//TODO: Implement LeaveRequestService
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository requestRepository;

    @Autowired
    public LeaveRequestServiceImpl(LeaveRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public LeaveRequest addNewLeaveRequest(LeaveRequest leaveRequest) {
        return null;
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequests() {
        return List.of();
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequestsByEmployeeId(Long employeeId) {
        return List.of();
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequestsByStatus(LeaveRequest.LeaveStatus status) {
        return List.of();
    }

    @Override
    public LeaveRequest getLeaveRequestById(Long leaveRequestId) {
        return null;
    }

    @Override
    public LeaveRequest getLeaveRequestByEmployeeIdAndDate(Long employeeId, LocalDate date) {
        return null;
    }

    @Override
    public LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest) {
        return null;
    }

    @Override
    public void deleteLeaveRequest(Long leaveRequestId) {

    }
}
