package com.motorph.ems.repository;

import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findAllByEmployee_EmployeeId(Long employeeId);

    List<Payroll> findAllByEmployeeEmployeeIdAndPeriodStartBetween(
            Long employeeId,
            LocalDate dateStart,
            LocalDate dateEnd
    );

    List<Payroll> findByPeriodEnd(LocalDate periodEnd);

    @Query("SELECT DISTINCT YEAR(p.periodEnd) FROM Payroll p ORDER BY YEAR(p.periodEnd)")
    List<Integer> findDistinctYears();
    @Query("SELECT DISTINCT p.periodEnd FROM Payroll p WHERE YEAR(p.periodEnd) = :year ORDER BY MONTH(p.periodEnd)")
    List<LocalDate> findDistinctMonthsByYear(@Param("year") int year);

    @Query("SELECT DATE_FORMAT(p.periodEnd, '%Y-%m-01') AS month, SUM(p.grossIncome) AS totalEarnings, (SUM(p.grossIncome) - SUM(p.netIncome)) AS totalDeductions " +
            "FROM Payroll p " +
            "WHERE p.periodEnd BETWEEN %:startDate% AND %:endDate% " +
            "GROUP BY DATE_FORMAT(p.periodEnd, '%Y-%m-01') " +
            "ORDER BY DATE_FORMAT(p.periodEnd, '%Y-%m-01') ASC")
    List<Object[]> getTotalEarningsAndDeductionsByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    boolean existsByEmployee_EmployeeIdAndPeriodStart(Long aLong, LocalDate localDate);

    Optional<Payroll> findByEmployee_EmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart);

    List<Payroll> findAllByPeriodStartAndPeriodEnd(LocalDate periodStart, LocalDate periodEnd);
}
