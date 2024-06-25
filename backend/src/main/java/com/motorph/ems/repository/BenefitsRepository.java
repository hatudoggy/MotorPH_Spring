package com.motorph.ems.repository;

import com.motorph.ems.model.Benefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BenefitsRepository extends JpaRepository<Benefits, Long> {

    List<Benefits> findByEmployee_EmployeeId(Long employeeId);

    List<Benefits> findAllByEmployee_FirstNameAndEmployee_LastName(String firstName, String lastName);

    List<Benefits> findAllByEmployee_Position_PositionCode(String positionCode);

    boolean existsByEmployee_EmployeeIdAndBenefitType_BenefitTypeId(Long employeeId, int benefitTypeId);

    Optional<Object> findAllByEmployee_EmployeeId(Long employeeId);
}
