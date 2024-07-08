package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.DepartmentDTO;
import com.motorph.pms.model.Department;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentMapper {

    public DepartmentDTO toDTO(Department department) {
        if (department == null) {
            return null;
        }
        return DepartmentDTO.builder()
                .departmentCode(department.getDepartmentCode())
                .departmentName(department.getDepartmentName())
                .build();
    }

    public Department toEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO.departmentCode() == null) {
            throw new IllegalArgumentException("Department code cannot be null when creating a new departmentName");
        }

        if (departmentDTO.departmentName() == null) {
            throw new IllegalArgumentException("Department name cannot be null when creating a new departmentName");
        }

        return new Department(
                departmentDTO.departmentCode(),
                departmentDTO.departmentName());
    }
}

