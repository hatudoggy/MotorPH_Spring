package com.motorph.pms.service;

import com.motorph.pms.dto.PayrollDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayrollService {
    PayrollDTO addNewPayroll(PayrollDTO payroll);
    
    Optional<PayrollDTO> getPayrollById(Long payrollId);

    List<PayrollDTO> getAllPayrolls(boolean isFullDetails);

    List<PayrollDTO> getPayrollsByEmployeeId(Long employeeId);

    List<PayrollDTO> getPayrollsForPeriod(boolean isFullDetails, LocalDate periodStart, LocalDate periodEnd);

    PayrollDTO updatePayroll(Long payrollId, PayrollDTO payroll);

    List<LocalDate> getDistinctMonthsByYear(int year);

    void deletePayroll(Long payrollId);

    List<Integer> getDistinctYears();

    int batchGeneratePayroll(LocalDate periodStart, LocalDate periodEnd);

    PayrollDTO generatePayroll(String startDate, String endDate, Long employeeId);

    List<PayrollDTO> getPayrollByEmployeeIdAndPeriodRange(long id, LocalDate start, LocalDate end);

    List<PayrollDTO> getPayrollsByDate(boolean isFullDetails, boolean isStartDate, LocalDate date);
}
