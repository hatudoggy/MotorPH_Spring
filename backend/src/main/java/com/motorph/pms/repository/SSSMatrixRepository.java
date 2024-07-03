package com.motorph.pms.repository;

import com.motorph.pms.model.SSSMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SSSMatrixRepository  extends JpaRepository<SSSMatrix, Long> {
    SSSMatrix findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(Double value, Double value2);
}