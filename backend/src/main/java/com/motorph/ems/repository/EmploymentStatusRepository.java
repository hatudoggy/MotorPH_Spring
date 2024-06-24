package com.motorph.ems.repository;

import com.motorph.ems.model.EmploymentStatus;
import com.motorph.ems.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentStatusRepository  extends JpaRepository<EmploymentStatus, Long> {
}
