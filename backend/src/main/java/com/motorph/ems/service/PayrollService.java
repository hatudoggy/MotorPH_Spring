package com.motorph.ems.service;

import com.motorph.ems.dto.PayrollDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayrollService {

    PayrollDTO addNewPayroll(PayrollDTO payroll);
    
    Optional<PayrollDTO> getPayrollById(Long payrollId);

    Optional<PayrollDTO> getPayrollByEmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart);

    List<PayrollDTO> getAllPayrolls();

    List<PayrollDTO> getPayrollsByEmployeeId(Long employeeId);

    List<PayrollDTO> getPayrollsForPeriod(LocalDate periodStart, LocalDate periodEnd);

    PayrollDTO updatePayroll(PayrollDTO payroll);

    void deletePayroll(Long payrollId);
}
