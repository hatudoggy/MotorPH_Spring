package com.motorph.ems.repository;

import com.motorph.ems.model.Deductions.DeductionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeductionTypeRepository extends JpaRepository<DeductionType, String> {
}