package com.motorph.ems.service;

import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.Payroll;

import java.time.LocalDate;
import java.util.List;

public interface PayrollService {
    Payroll addNewPayroll(Payroll payroll);

    void batchAddPayroll();

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
}
