package com.motorph.ems.service.impl;

import com.motorph.ems.model.Payroll;
import com.motorph.ems.repository.PayrollRepository;
import com.motorph.ems.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

//TODO: Implement Payroll Service
@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;

    @Autowired
    public PayrollServiceImpl(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    @Override
    public Payroll addNewPayroll(Payroll payroll) {
        return null;
    }

    @Override
    public List<Payroll> getAllPayrolls() {
        return List.of();
    }

    @Override
    public Payroll getPayrollById(Long payrollId) {
        return null;
    }

    @Override
    public Payroll getPayrollByEmployeeIdAndPeriodDates(Long employeeId, LocalDate start, LocalDate end) {
        return null;
    }

    @Override
    public Payroll updatePayroll(Payroll payroll) {
        return null;
    }

    @Override
    public void deletePayroll(Long payrollId) {

    }
}
