package com.motorph.ems.repository;

import com.motorph.ems.model.Department;
import com.motorph.ems.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository  extends JpaRepository<Department, Long> {
}
