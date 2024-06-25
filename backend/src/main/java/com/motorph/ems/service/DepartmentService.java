package com.motorph.ems.service;

import com.motorph.ems.dto.DepartmentDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    DepartmentDTO addNewDepartment(DepartmentDTO departmentDTO);

    List<DepartmentDTO> getDepartments();

    Optional<DepartmentDTO> getDepartmentByDepartmentCode(String departmentCode);

    Optional<DepartmentDTO> getDepartmentByName(String departmentName);
}
