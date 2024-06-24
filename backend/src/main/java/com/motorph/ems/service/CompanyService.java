package com.motorph.ems.service;

import com.motorph.ems.model.Department;
import com.motorph.ems.model.EmploymentStatus;
import com.motorph.ems.model.Position;

import java.util.List;

public interface CompanyService {

    List<Position> getPositions();
    List<Position> getPositionsByDepartment(String departmentCode);
    List<Department> getDepartments();
    List<EmploymentStatus> getEmploymentStatuses();
}
