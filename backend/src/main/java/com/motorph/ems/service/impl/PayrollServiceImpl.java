package com.motorph.ems.service.impl;

import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.Benefits;
import com.motorph.ems.model.Employee;
import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.Payroll;
import com.motorph.ems.repository.AttendanceRepository;
import com.motorph.ems.repository.EmployeeRepository;
import com.motorph.ems.repository.PayrollRepository;
import com.motorph.ems.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//TODO: Implement Payroll Service
@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public PayrollServiceImpl(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository
    ) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Payroll addNewPayroll(Payroll payroll) {
        return payrollRepository.save(payroll);
    }

    @Override
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    @Override
    public List<Payroll> getAllPayrollsByDate(LocalDate date) {
        return payrollRepository.findByPeriodEnd(date);
    }

    @Override
    public List<PayrollDTO> getAllPayrollsDTO() {
        return payrollRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PayrollDTO> getAllPayrollsDTOByDate(LocalDate date) {
        return payrollRepository.findByPeriodEnd(date).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Payroll getPayrollById(Long payrollId) {
        return payrollRepository.findById(payrollId).orElse(null);
    }

    @Override
    public List<Payroll> getPayrollsByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeEmployeeId(employeeId);
    }

    @Override
    public List<Payroll> getPayrollByEmployeeIdAndPeriodDates(Long employeeId, LocalDate start, LocalDate end) {
        return payrollRepository.findAllByEmployeeEmployeeIdAndPeriodStartBetween(employeeId, start, end);
    }

    @Override
    public List<Integer> getDistinctYear() {
        return payrollRepository.findDistinctYears();
    }

    @Override
    public List<LocalDate> getDistinctMonthsByYear(int year) {
        return payrollRepository.findDistinctMonthsByYear(year);
    }

    @Override
    public Payroll updatePayroll(Payroll payroll) {
        return payrollRepository.save(payroll);
    }

    @Override
    public void deletePayroll(Long payrollId) {

    }

    private PayrollDTO convertToDTO(Payroll payroll) {
        PayrollDTO dto = new PayrollDTO();
        dto.setPayrollId(payroll.getPayrollId());
        dto.setEmployeeId(payroll.getEmployee().getEmployeeId());
        dto.setEmployeeFirstName(payroll.getEmployee().getFirstName());
        dto.setEmployeeLastName(payroll.getEmployee().getLastName());
        dto.setPeriodStart(payroll.getPeriodStart());
        dto.setPeriodEnd(payroll.getPeriodEnd());
        dto.setMonthlyRate(payroll.getMonthlyRate());
        dto.setDailyRate(payroll.getDailyRate());
        dto.setOvertimePay(payroll.getOvertimePay());
        dto.setGrossIncome(payroll.getGrossIncome());
        dto.setNetIncome(payroll.getNetIncome());
        dto.setDeductions(payroll.getDeductions());
        return dto;
    }


    @Override
    public void batchGeneratePayroll(LocalDate periodStart, LocalDate periodEnd) {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            Long presentCount = attendanceRepository
                    .countPresentAttendancesByEmployeeId(employee.getEmployeeId(), periodStart, periodEnd);
            Long overtimeHours = attendanceRepository
                    .sumOvertimeHoursByEmployeeId(employee.getEmployeeId(), periodStart, periodEnd);
            double hourlyRate = employee.getEmployment().getHourlyRate();
            double dailyRate = hourlyRate * 8;

            List<Benefits> benefits = employee.getBenefits();

            Payroll payroll = new Payroll();
            payroll.setEmployee(employee);
            payroll.setPeriodStart(periodStart);
            payroll.setPeriodEnd(periodEnd);
            payroll.setMonthlyRate(dailyRate * presentCount);
            payroll.setDailyRate(dailyRate);
            payroll.setOvertimePay(hourlyRate * overtimeHours);
            payroll.setGrossIncome(hourlyRate * overtimeHours);
        }
    }
}
