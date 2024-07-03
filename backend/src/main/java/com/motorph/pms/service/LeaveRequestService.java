package com.motorph.pms.service;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/service/LeaveRequestService.java
import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.LeaveRequest.LeaveStatus;
=======
import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.dto.LeaveStatusDTO;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/service/LeaveRequestService.java

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
