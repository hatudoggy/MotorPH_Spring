package com.motorph.ems.dto.mapper;

import com.motorph.ems.dto.DepartmentDTO;
import com.motorph.ems.model.Department;
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
                .department(department.getDepartmentName())
                .build();
    }

    public Department toEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO.departmentCode() == null) {
            throw new IllegalArgumentException("Department code cannot be null when creating a new department");
        }

        if (departmentDTO.department() == null) {
            throw new IllegalArgumentException("Department name cannot be null when creating a new department");
        }

        return new Department(
                departmentDTO.departmentCode(),
                departmentDTO.department());
    }

    public List<DepartmentDTO> toDTO(List<Department> departments) {
        if (departments == null) {
            return null;
        }

        return departments.stream()
                .map(this::toDTO)
                .toList();
    }
}

