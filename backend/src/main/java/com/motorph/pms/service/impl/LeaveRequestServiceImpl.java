package com.motorph.pms.service.impl;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/service/impl/LeaveRequestServiceImpl.java
import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.repository.LeaveRequestRepository;
import com.motorph.ems.service.LeaveRequestService;
=======
import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.dto.LeaveStatusDTO;
import com.motorph.pms.dto.mapper.LeaveRequestMapper;
import com.motorph.pms.model.LeaveRequest;
import com.motorph.pms.repository.LeaveRequestRepository;
import com.motorph.pms.repository.LeaveStatusRepository;
import com.motorph.pms.service.LeaveRequestService;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/service/impl/LeaveRequestServiceImpl.java
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
        return requestRepository.save(leaveRequest);
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequests() {
        return requestRepository.findAll();
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequestsByEmployeeId(Long employeeId) {
        return requestRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequestsByStatus(LeaveRequest.LeaveStatus status) {
        return List.of();
    }

    @Override
    public LeaveRequest getLeaveRequestById(Long leaveRequestId) {
        return requestRepository.findById(leaveRequestId).orElse(null);
    }

    @Override
    public LeaveRequest getLeaveRequestByEmployeeIdAndDate(Long employeeId, LocalDate date) {
        return null;
    }

    @Override
    public LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest) {
        return requestRepository.save(leaveRequest);
    }

    @Override
    public void deleteLeaveRequest(Long leaveRequestId) {

    }
}
