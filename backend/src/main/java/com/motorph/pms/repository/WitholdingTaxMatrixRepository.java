package com.motorph.pms.repository;

import com.motorph.pms.model.WitholdingTaxMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WitholdingTaxMatrixRepository  extends JpaRepository<WitholdingTaxMatrix, Long> {
    WitholdingTaxMatrix findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(Double value, Double value2);
}
