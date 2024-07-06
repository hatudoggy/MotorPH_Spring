package com.motorph.pms.repository;

import com.motorph.pms.model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> getLeaveBalanceByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(Long employee_employeeId, int leaveTypeId);

    List<LeaveBalance> findAllByEmployee_EmployeeId(Long employeeId);
}
