package com.motorph.pms.repository;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/repository/WitholdingTaxMatrixRepository.java
import com.motorph.pms.model.PagibigMatrix;
import com.motorph.pms.model.WitholdingTaxMatrix;
=======
import com.motorph.pms.model.WitholdingTaxMatrix;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/repository/WitholdingTaxMatrixRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WitholdingTaxMatrixRepository  extends JpaRepository<WitholdingTaxMatrix, Long> {
    WitholdingTaxMatrix findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(Double value, Double value2);
}
