package com.motorph.pms.service;

import com.motorph.pms.service.extended.*;

public interface CompanyService extends
        EmploymentStatusService,
        PositionService,
        DepartmentService,
        BenefitTypesService,
        LeaveTypesService,
        LeaveStatusesService,
        UserRolesService{}
