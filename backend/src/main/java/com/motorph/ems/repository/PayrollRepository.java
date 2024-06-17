package com.motorph.ems.repository;

import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeId(Long employeeId);
    List<Payroll> findAllByEmployeeIdAndPeriodStartBetween(
            Long employeeId,
            LocalDate dateStart,
            LocalDate dateEnd
    );
}
