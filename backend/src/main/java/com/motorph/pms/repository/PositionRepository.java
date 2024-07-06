package com.motorph.pms.repository;

import com.motorph.pms.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, String> {
    List<Position> findAllByDepartment_DepartmentCode(String departmentCode);
}
