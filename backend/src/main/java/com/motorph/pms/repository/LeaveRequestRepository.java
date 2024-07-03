package com.motorph.pms.repository;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/repository/LeaveRequestRepository.java
import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.LeaveRequest;
=======
import com.motorph.pms.model.LeaveRequest;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/repository/LeaveRequestRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);
}
