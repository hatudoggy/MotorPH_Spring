package com.motorph.pms.repository;

import com.motorph.pms.model.LeaveBalance;
import com.motorph.pms.model.LeaveBalance.LeaveType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    @EntityGraph(attributePaths = {"leaveType", "employee", "employee.position", "employee.department"})
    Optional<LeaveBalance> getLeaveBalanceByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(Long employee_employeeId, int leaveTypeId);

    @EntityGraph(attributePaths = {"leaveType", "employee", "employee.position", "employee.department"})
    List<LeaveBalance> findAllByEmployee_EmployeeId(Long employeeId);

    @EntityGraph(attributePaths = {"leaveType", "employee", "employee.position", "employee.department"})
    Optional<LeaveBalance> findAllByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(Long employeeId, int leaveTypeId);
}
