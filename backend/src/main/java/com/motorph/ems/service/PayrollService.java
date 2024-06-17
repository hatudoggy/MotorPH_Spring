package com.motorph.ems.service;

import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.Payroll;

import java.time.LocalDate;
import java.util.List;

public interface PayrollService {
    Payroll addNewPayroll(Payroll payroll);

    void batchAddPayroll();

    List<Payroll> getAllPayrolls();

    Payroll getPayrollById(Long payrollId);

    List<Payroll> getPayrollsByEmployeeId(Long employeeId);

    List<Payroll> getPayrollByEmployeeIdAndPeriodDates(Long employeeId, LocalDate start, LocalDate end);

    Payroll updatePayroll(Payroll payroll);

    void deletePayroll(Long payrollId);
}
