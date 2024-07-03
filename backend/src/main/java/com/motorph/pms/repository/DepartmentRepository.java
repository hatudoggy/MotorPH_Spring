package com.motorph.ems.repository;

import com.motorph.pms.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    Optional<Department> findByDepartmentName(String departmentName);
}
