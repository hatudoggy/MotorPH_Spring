package com.motorph.ems.repository;

import com.motorph.ems.model.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmploymentStatusRepository extends JpaRepository<EmploymentStatus, Integer> {

    Optional<EmploymentStatus> findByStatusName(String status);
}
