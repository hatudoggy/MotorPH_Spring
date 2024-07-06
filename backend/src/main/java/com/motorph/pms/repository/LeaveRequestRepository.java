package com.motorph.pms.repository;

import com.motorph.pms.model.LeaveRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findAllByEmployee_EmployeeId(Long employeeId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM LeaveRequest r " +
            "WHERE r.employee.employeeId = :employeeId " +
            "AND ((r.startDate BETWEEN :startDate AND :endDate) " +
            "OR (r.endDate BETWEEN :startDate AND :endDate))")
    boolean existsByEmployeeIdAndDateRange(
            @Param("employee") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
