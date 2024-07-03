package com.motorph.pms.service;

import com.motorph.pms.dto.DepartmentDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<DepartmentDTO> getDepartments();

    Optional<DepartmentDTO> getDepartmentByDepartmentCode(String departmentCode);
}
