package com.motorph.pms.repository;

import com.motorph.pms.model.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmploymentStatusRepository extends JpaRepository<EmploymentStatus, Integer> {

    Optional<EmploymentStatus> findByStatusName(String status);
}
