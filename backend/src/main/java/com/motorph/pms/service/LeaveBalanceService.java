package com.motorph.pms.service;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/service/LeaveBalanceService.java
import com.motorph.ems.model.LeaveBalance;
=======
import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveTypeDTO;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/service/LeaveBalanceService.java

import java.util.List;

public interface LeaveBalanceService {

    LeaveBalance addNewLeaveBalance(LeaveBalance leaveBalance);

    List<LeaveBalance> addMultipleLeaveBalances(List<LeaveBalance> leaveBalances);

    LeaveBalance getLeaveBalanceById(Long employeeId);

    LeaveBalance getLeaveBalanceByEmployeeIdAndLeaveType(Long employeeId, String leaveType);

    List<LeaveBalance> getAllLeaveBalances();

    List<LeaveBalance> getLeaveBalancesByEmployeeId(Long employeeId);

    LeaveBalance updateLeaveBalance(LeaveBalance leaveBalance);

    void deleteLeaveBalance(Long employeeId);
}
