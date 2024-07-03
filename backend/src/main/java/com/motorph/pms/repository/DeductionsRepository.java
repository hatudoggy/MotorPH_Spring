package com.motorph.ems.repository;

import com.motorph.pms.model.Deductions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DeductionsRepository  extends JpaRepository<Deductions, Long> {
}
