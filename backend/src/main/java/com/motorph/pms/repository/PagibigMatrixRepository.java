package com.motorph.ems.repository;

import com.motorph.ems.model.PagibigMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagibigMatrixRepository  extends JpaRepository<PagibigMatrix, Long> {
   PagibigMatrix findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(Double value, Double value2);
}
