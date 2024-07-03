package com.motorph.ems.service.impl;

import com.motorph.pms.model.Department;
import com.motorph.pms.model.EmploymentStatus;
import com.motorph.pms.model.Position;
import com.motorph.pms.repository.AttendanceRepository;
import com.motorph.pms.repository.DepartmentRepository;
import com.motorph.pms.repository.EmploymentStatusRepository;
import com.motorph.pms.repository.PositionRepository;
import com.motorph.pms.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmploymentStatusRepository employmentStatusRepository;

    @Autowired
    public CompanyServiceImpl(
            PositionRepository positionRepository,
            DepartmentRepository departmentRepository,
            EmploymentStatusRepository employmentStatusRepository
    ) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.employmentStatusRepository = employmentStatusRepository;
    }

    @Override
    public List<Position> getPositions() {
        return positionRepository.findAll();
    }

    @Override
    public List<Position> getPositionsByDepartment(String departmentCode) {
        return positionRepository.findAllByDepartmentCode(departmentCode);
    }

    @Override
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public List<EmploymentStatus> getEmploymentStatuses() {
        return employmentStatusRepository.findAll();
    }
}
