package com.motorph.pms.service;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/service/PayrollService.java
import com.motorph.ems.dto.MonthlyPayrollReportDTO;
import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.Payroll;
=======
import com.motorph.pms.dto.MonthlyPayrollReportDTO;
import com.motorph.pms.dto.PayrollDTO;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/service/PayrollService.java

import java.time.LocalDate;
import java.util.List;

public interface PayrollService {
    Payroll addNewPayroll(Payroll payroll);

    List<Payroll> getAllPayrolls();

    List<Payroll> getAllPayrollsByDate(LocalDate date);

    List<PayrollDTO> getAllPayrollsDTO();

    List<PayrollDTO> getAllPayrollsDTOByDate(LocalDate date);

    Payroll getPayrollById(Long payrollId);

    List<Payroll> getPayrollsByEmployeeId(Long employeeId);

    List<Payroll> getPayrollByEmployeeIdAndPeriodDates(Long employeeId, LocalDate start, LocalDate end);

    List<Integer> getDistinctYear();

    List<LocalDate> getDistinctMonthsByYear(int year);

    Payroll updatePayroll(Payroll payroll);

    void deletePayroll(Long payrollId);

    List<MonthlyPayrollReportDTO> getMonthlyReport(LocalDate start, LocalDate end);

    void batchGeneratePayroll(LocalDate periodStart, LocalDate periodEnd);

}
