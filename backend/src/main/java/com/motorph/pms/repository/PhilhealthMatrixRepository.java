package com.motorph.ems.repository;

import com.motorph.ems.model.PhilhealthMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhilhealthMatrixRepository  extends JpaRepository<PhilhealthMatrix, Long> {
    PhilhealthMatrix findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(Double value, Double value2);
}