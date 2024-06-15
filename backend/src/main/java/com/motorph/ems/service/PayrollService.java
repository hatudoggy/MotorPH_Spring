package com.motorph.ems.service;

import com.motorph.ems.model.Payroll;

import java.time.LocalDate;
import java.util.List;

public interface PayrollService {
    Payroll addNewPayroll(Payroll payroll);

    List<Payroll> getAllPayrolls();

    Payroll getPayrollById(Long payrollId);

    Payroll getPayrollByEmployeeIdAndPeriodDates(Long employeeId, LocalDate start, LocalDate end);

    Payroll updatePayroll(Payroll payroll);

    void deletePayroll(Long payrollId);
}
