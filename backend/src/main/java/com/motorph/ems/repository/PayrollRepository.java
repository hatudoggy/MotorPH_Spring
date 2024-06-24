package com.motorph.ems.repository;

import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeEmployeeId(Long employeeId);
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
}
