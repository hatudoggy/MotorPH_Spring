package com.motorph.pms.service.extended;

import com.motorph.pms.dto.LeaveStatusDTO;

import java.util.List;

public interface LeaveStatusesService {
    List<LeaveStatusDTO> getLeaveStatuses();

    LeaveStatusDTO getLeaveStatus(int id);
}
