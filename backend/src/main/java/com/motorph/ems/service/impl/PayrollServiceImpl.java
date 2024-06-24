package com.motorph.ems.service.impl;

import com.motorph.ems.dto.MonthlyPayrollReportDTO;
import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.*;
import com.motorph.ems.repository.AttendanceRepository;
import com.motorph.ems.repository.EmployeeRepository;
import com.motorph.ems.repository.PayrollRepository;
import com.motorph.ems.service.MatrixService;
import com.motorph.ems.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO: Implement Payroll Service
@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final MatrixService matrixService;

    @Autowired
    public PayrollServiceImpl(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository,
            MatrixService matrixService
    ) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.matrixService = matrixService;
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
    public List<MonthlyPayrollReportDTO> getMonthlyReport(LocalDate start, LocalDate end) {
        List<Object[]> results = payrollRepository.getTotalEarningsAndDeductionsByMonth(start, end);
        List<MonthlyPayrollReportDTO> dtoList = new ArrayList<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        for (Object[] result : results) {
            LocalDate monthYear = LocalDate.parse((String) result[0]);
            double totalEarnings = (Double) result[1];
            double totalDeductions = (Double) result[2];

            MonthlyPayrollReportDTO dto = new MonthlyPayrollReportDTO(monthYear, totalEarnings, totalDeductions);
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public void batchGeneratePayroll(LocalDate periodStart, LocalDate periodEnd) {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            Long presentCount = attendanceRepository
                    .countPresentAttendancesByEmployeeId(employee.getEmployeeId(), periodStart, periodEnd);
            Long overtimeHours = attendanceRepository
                    .sumOvertimeHoursByEmployeeId(employee.getEmployeeId(), periodStart, periodEnd);
            double basicSalary = employee.getEmployment().getBasicSalary();
            double hourlyRate = employee.getEmployment().getHourlyRate();
            double dailyRate = round(hourlyRate * 8, 2);
            double monthlyRate = round(dailyRate * presentCount, 2);
            double overtimePay = round(hourlyRate * overtimeHours, 2);
            double benefits = employee.getBenefits().stream().mapToDouble(Benefits::getAmount).sum();
            double grossIncome = round(monthlyRate + overtimePay + benefits, 2);

            double sss = matrixService.getSSSMatrix(basicSalary).getContribution();
            PhilhealthMatrix phm = matrixService.getPhilhealthMatrix(basicSalary);
            double philhealth = Math.floor(
                    (phm.getMonthlyPremiumCap() == null ?
                            phm.getMonthlyPremiumBase() :
                            basicSalary * phm.getPremiumRate()
                            ) / 2 * 100) / 100;
            PagibigMatrix pgm = matrixService.getPagibigMatrix(basicSalary);
            double pagibig = Math.max((basicSalary * pgm.getTotalRate()), 100);
            WitholdingTaxMatrix tm = matrixService.getWitholdingTaxMatrix(basicSalary);
            double witholdingTax = (tm.getTaxBase() + basicSalary - tm.getMinRange()) * tm.getTaxRate();
            double deductions = sss + philhealth + pagibig + witholdingTax;

            double netPay = round(grossIncome - deductions, 2);

            Payroll payroll = new Payroll();
            payroll.setEmployee(employee);
            payroll.setPeriodStart(periodStart);
            payroll.setPeriodEnd(periodEnd);
            payroll.setMonthlyRate(monthlyRate);
            payroll.setDailyRate(dailyRate);
            payroll.setOvertimePay(overtimePay);
            payroll.setGrossIncome(grossIncome);
            payroll.setNetIncome(netPay);
            payroll.setNetPay(netPay);

            payrollRepository.save(payroll);

            saveDeductions(payroll, sss, philhealth, pagibig, witholdingTax);
        }
    }

    private void saveDeductions(Payroll payroll, double sss, double philhealth, double pagibig, double witholdingTax) {
        Deductions sssDeduction = createDeduction(payroll, sss, "SSS");
        Deductions philhealthDeduction = createDeduction(payroll, philhealth, "PHIC");
        Deductions pagibigDeduction = createDeduction(payroll, pagibig, "HDMF");
        Deductions withholdingTaxDeduction = createDeduction(payroll, witholdingTax, "TAX");

        payroll.setDeductions(new ArrayList<>());
        payroll.getDeductions().add(sssDeduction);
        payroll.getDeductions().add(philhealthDeduction);
        payroll.getDeductions().add(pagibigDeduction);
        payroll.getDeductions().add(withholdingTaxDeduction);

        payrollRepository.save(payroll);
    }

    private Deductions createDeduction(Payroll payroll, double amount, String deductionCode) {
        Deductions deduction = new Deductions();
        deduction.setPayroll(payroll);
        deduction.setAmount(Math.round(amount));

        Deductions.DeductionType deductionType = new Deductions.DeductionType();
        deductionType.setDeductionCode(deductionCode);
        deduction.setDeductionType(deductionType);

        return deduction;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
