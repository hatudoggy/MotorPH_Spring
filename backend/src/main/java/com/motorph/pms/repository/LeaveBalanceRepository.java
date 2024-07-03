package com.motorph.pms.repository;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/repository/LeaveBalanceRepository.java
import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.LeaveBalance;
=======
import com.motorph.pms.model.LeaveBalance;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/repository/LeaveBalanceRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    List<LeaveBalance> findByEmployeeId(Long employeeId);
}
