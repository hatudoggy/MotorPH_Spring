package com.motorph.ems.repository;

import com.motorph.ems.model.Employment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentRepository extends
        JpaRepository<Employment, Long> {
}
