package com.motorph.ems.repository;

import com.motorph.pms.model.Attendance;
import com.motorph.pms.model.Benefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitsRepository extends JpaRepository<Benefits, Long> {
    List<Benefits> findByEmployeeEmployeeId(Long employeeId);
}
