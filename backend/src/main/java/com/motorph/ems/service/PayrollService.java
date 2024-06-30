package com.motorph.ems.service;

import com.motorph.ems.dto.MonthlyPayrollReportDTO;
import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.Payroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayrollService {

    PayrollDTO addNewPayroll(PayrollDTO payroll);
    
    Optional<PayrollDTO> getPayrollById(Long payrollId);

    Optional<PayrollDTO> getPayrollByEmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart);

    List<PayrollDTO> getAllPayrolls(boolean isFullDetails);

    List<PayrollDTO> getPayrollsByEmployeeId(Long employeeId);

    List<PayrollDTO> getPayrollsForPeriod(boolean isFullDetails, LocalDate periodStart, LocalDate periodEnd);

    PayrollDTO updatePayroll(Long payrollId, PayrollDTO payroll);

    List<LocalDate> getDistinctMonthsByYear(int year);

    void deletePayroll(Long payrollId);

    List<Integer> getDistinctYears();

    List<MonthlyPayrollReportDTO> getMonthlyReport(LocalDate start, LocalDate end);

    void batchGeneratePayroll(LocalDate periodStart, LocalDate periodEnd);

    List<PayrollDTO> getPayrollByEmployeeIdAndPeriodRange(long id, LocalDate start, LocalDate end);

    List<PayrollDTO> getPayrollsByDate(boolean isFullDetails, boolean isStartDate, LocalDate date);
}
