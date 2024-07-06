package com.motorph.pms.service.extended;

import com.motorph.pms.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getDepartments();

    DepartmentDTO getDepartment(String departmentCode);
}
