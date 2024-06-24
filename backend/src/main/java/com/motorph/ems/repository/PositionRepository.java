package com.motorph.ems.repository;

import com.motorph.ems.model.Position;
import com.motorph.ems.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findAllByDepartmentCode(String departmentCode);
}
