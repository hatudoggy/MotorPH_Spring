package com.motorph.ems.repository;

import com.motorph.ems.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Optional<LeaveRequest> findByEmployee_EmployeeIdAndRequestDate(Long employeeId, LocalDate date);

    List<LeaveRequest> findAllByEmployee_EmployeeId(Long employeeId);

    List<LeaveRequest> findAllByEmployee_FirstNameAndEmployee_LastName(String firstName, String lastName);

    List<LeaveRequest> findAllByEmployee_Position_PositionCode(String positionCode);

    List<LeaveRequest> findAllByEmployee_Department_DepartmentCode(String departmentCode);

    List<LeaveRequest> findAllByStatus_StatusName(String status);

    List<LeaveRequest> findAllByRequestDate(LocalDate requestDate);

    List<LeaveRequest> findAllByStartDate(LocalDate startDate);

    List<LeaveRequest> findAllByRequestDateBetween(LocalDate startDate, LocalDate endDate);

    List<LeaveRequest> findAllByStatus_StatusNameAndRequestDateBetween(String status, LocalDate startDate, LocalDate endDate);

    List<LeaveRequest> findAllByEmployee_Supervisor_EmployeeId(Long supervisorId);

    List<LeaveRequest> findByEmployee_Supervisor_FirstNameAndEmployee_Supervisor_LastName(String supervisor_firstName, String supervisor_lastName);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM LeaveRequest r " +
            "WHERE r.employee.employeeId = :employeeId " +
            "AND ((r.startDate BETWEEN :startDate AND :endDate) " +
            "OR (r.endDate BETWEEN :startDate AND :endDate))")
    boolean existsByEmployeeIdAndDateRange(
            @Param("employee") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
