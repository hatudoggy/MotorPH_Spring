package com.motorph.ems.service;

import com.motorph.pms.model.Department;
import com.motorph.pms.model.EmploymentStatus;
import com.motorph.pms.model.Position;

import java.util.List;

public interface CompanyService {

    List<Position> getPositions();
    List<Position> getPositionsByDepartment(String departmentCode);
    List<Department> getDepartments();
    List<EmploymentStatus> getEmploymentStatuses();
}
