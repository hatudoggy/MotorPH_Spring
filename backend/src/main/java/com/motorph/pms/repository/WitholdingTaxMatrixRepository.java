package com.motorph.pms.repository;

import com.motorph.pms.model.WithholdingTaxMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WitholdingTaxMatrixRepository  extends JpaRepository<WithholdingTaxMatrix, Long> {
    WithholdingTaxMatrix findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(Double value, Double value2);
}
