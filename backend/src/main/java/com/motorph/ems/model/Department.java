package com.motorph.ems.model;

import com.motorph.ems.dto.DepartmentDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "department")
public class Department {
    @Id
    private String departmentCode;
    private String departmentName;

    public Department(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public DepartmentDTO toDTO() {
        if ( departmentCode == null || departmentName == null) {
            return null;
        }

        return DepartmentDTO.builder()
                .departmentCode(departmentCode)
                .department(departmentName)
                .build();
    }
    public static Department fromDTO(DepartmentDTO department) {
        if (department == null) {
            return null;
        }

        return Department.builder()
                .departmentCode(department.departmentCode())
                .departmentName(department.department())
                .build();
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentCode='" + departmentCode + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
