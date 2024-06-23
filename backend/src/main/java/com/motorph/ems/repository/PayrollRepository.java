package com.motorph.ems.repository;

import com.motorph.ems.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    Optional<Payroll> findByEmployee_EmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart);

    List<Payroll> findAllByEmployee_EmployeeId(Long employeeId);

    List<Payroll> findAllByPeriodStartAndPeriodEnd(LocalDate periodStart, LocalDate periodEnd);

    boolean existsByEmployee_EmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart);
}
