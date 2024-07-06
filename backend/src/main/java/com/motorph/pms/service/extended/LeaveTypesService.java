package com.motorph.pms.service.extended;

import com.motorph.pms.dto.LeaveTypeDTO;

import java.util.List;

public interface LeaveTypesService {
    List<LeaveTypeDTO> getLeaveTypes();
    LeaveTypeDTO getLeaveType(int id);
}
