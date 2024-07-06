package com.motorph.pms.service;

import com.motorph.pms.dto.SupervisorDTO;
import com.motorph.pms.service.extended.*;

import java.util.List;

public interface CompanyService extends
        EmploymentStatusService,
        PositionService,
        DepartmentService,
        BenefitTypesService,
        LeaveTypesService,
        LeaveStatusesService,
        UserRolesService{

    List<SupervisorDTO> getSupervisors();
    SupervisorDTO getSupervisor(Long supervisorId);
}
