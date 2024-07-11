package com.motorph.pms.repository;

import com.motorph.pms.model.LeaveRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @EntityGraph(attributePaths = {"employee", "employee.position", "employee.department"})
    List<LeaveRequest> findAllByEmployee_EmployeeId(Long employeeId);

    boolean existsByEmployee_EmployeeIdAndStartDateAfterAndEndDateBefore(Long employee_employeeId, LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"employee", "employee.position", "employee.department"})
    List<LeaveRequest> findAllByStartDateAfterAndEndDateBefore(LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"employee", "employee.position", "employee.department"})
    List<LeaveRequest> findAllByRequestDateAfterAndRequestDateBefore(LocalDate startDate, LocalDate endDate);
}
