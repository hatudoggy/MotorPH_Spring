package com.motorph.ems.repository;

import com.motorph.pms.model.Employment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentRepository extends
        JpaRepository<Employment, Long> {
}
