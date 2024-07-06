package com.motorph.pms.repository;

import com.motorph.pms.model.Payroll;
import org.springframework.cache.annotation.Cacheable;
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

    @Query("SELECT DISTINCT YEAR(p.periodEnd) FROM Payroll p ORDER BY YEAR(p.periodEnd)")
    List<Integer> findDistinctYears();

    @Query("SELECT DISTINCT p.periodEnd FROM Payroll p WHERE YEAR(p.periodEnd) = :year ORDER BY MONTH(p.periodEnd)")
    List<LocalDate> findDistinctMonthsByYear(@Param("year") int year);

    boolean existsByEmployee_EmployeeIdAndPeriodStart(Long aLong, LocalDate localDate);

    Optional<Payroll> findByEmployee_EmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart);

    List<Payroll> findAllByPeriodStartAndPeriodEnd(LocalDate periodStart, LocalDate periodEnd);

    List<Payroll> findAllByEmployeeEmployeeIdAndPeriodStartAndPeriodEnd(long id, LocalDate start, LocalDate end);

    List<Payroll> findAllByPeriodStart(LocalDate date);

    List<Payroll> findAllByPeriodEnd(LocalDate date);
}
