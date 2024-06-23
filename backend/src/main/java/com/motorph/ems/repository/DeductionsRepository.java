package com.motorph.ems.repository;

import com.motorph.ems.model.Deductions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DeductionsRepository extends JpaRepository<Deductions, Long> {

    List<Deductions> findAllByPayroll_PayrollId(Long payrollId);

    List<Deductions> findAllByDeductionType_DeductionCode(String deductionCode);

    List<Deductions> findAllByPayroll_PeriodStart(LocalDate periodStart);

    boolean existsByPayroll_PayrollId(Long payrollId);

    List<Deductions> findAllByPayroll_Employee_EmployeeId(Long employeeId);
}
