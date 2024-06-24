package com.motorph.ems.service.impl;

import com.motorph.ems.dto.MonthlyPayrollReportDTO;
import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.dto.mapper.PayrollMapper;
import com.motorph.ems.model.*;
import com.motorph.ems.model.Deductions.DeductionType;
import com.motorph.ems.repository.EmployeeRepository;
import com.motorph.ems.repository.PayrollRepository;
import com.motorph.ems.service.AttendanceService;
import com.motorph.ems.service.MatrixService;
import com.motorph.ems.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceService attendanceService;
    private final MatrixService matrixService;
    private final PayrollMapper payrollMapper;

    @Autowired
    public PayrollServiceImpl(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository,
            AttendanceService attendanceService,
            MatrixService matrixService,
            PayrollMapper payrollMapper) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceService = attendanceService;
        this.matrixService = matrixService;
        this.payrollMapper = payrollMapper;
    }

    @Override
    public PayrollDTO addNewPayroll(PayrollDTO payrollDTO) {
        if (payrollRepository.existsByEmployee_EmployeeIdAndPeriodStart(
                payrollDTO.employeeId(), payrollDTO.periodStart())) {
            throw new IllegalStateException("Payroll with employeeId " + payrollDTO.employeeId() +
                    " and period start " + payrollDTO.periodStart() + " already exists");
        }

        Payroll payroll = payrollMapper.toEntity(payrollDTO);
        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Override
    public List<PayrollDTO> getAllPayrolls() {
        return payrollRepository.findAll().stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PayrollDTO> getPayrollsByEmployeeId(Long employeeId) {
        return payrollRepository.findAllByEmployee_EmployeeId(employeeId)
                .stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PayrollDTO> getPayrollsForPeriod(LocalDate periodStart, LocalDate periodEnd) {
        return payrollRepository.findAllByPeriodStartAndPeriodEnd(periodStart, periodEnd)
                .stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PayrollDTO> getPayrollById(Long payrollId) {
        return payrollRepository.findById(payrollId).map(payrollMapper::toDTO);
    }

    @Override
    public Optional<PayrollDTO> getPayrollByEmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart) {
        return payrollRepository.findByEmployee_EmployeeIdAndPeriodStart(employeeId, periodStart)
                .map(payrollMapper::toDTO);
    }

    @Override
    public PayrollDTO updatePayroll(Long payrollId, PayrollDTO payrollDTO) {
        Payroll existingPayroll = payrollRepository.findById(payrollId).orElseThrow(
                () -> new IllegalStateException("Payroll with payrollId " + payrollDTO.payrollId() + " does not exist"
        ));

        payrollMapper.updateEntity(payrollDTO, existingPayroll);

        return payrollMapper.toDTO(payrollRepository.save(existingPayroll));
    }

    @Override
    public List<LocalDate> getDistinctMonthsByYear(int year) {
        return payrollRepository.findDistinctMonthsByYear(year);
    }

    @Override
    public void deletePayroll(Long payrollId) {
        if (!payrollRepository.existsById(payrollId)) {
            throw new IllegalStateException("Payroll with payrollId " + payrollId + " does not exist");
        }

        payrollRepository.deleteById(payrollId);
    }

    @Override
    public List<Integer> getDistinctYears() {
        return payrollRepository.findDistinctYears();
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
        List<Employee> employees = employeeRepository.findAllExceptByStatus_StatusId(3);

        for (Employee employee : employees) {
            Long presentCount = attendanceService
                    .countPresentAttendancesByEmployeeId(employee.getEmployeeId(), periodStart, periodEnd);
            double overtimeHours = attendanceService
                    .calculateOvertimeHoursByEmployeeIdAndDateRange(employee.getEmployeeId(), periodStart, periodEnd);
            double basicSalary = employee.getBasicSalary();
            double hourlyRate = employee.getHourlyRate();
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

            Payroll payroll = new Payroll(
                    employee.getEmployeeId(),
                    periodStart,
                    periodEnd,
                    monthlyRate,
                    dailyRate,
                    overtimePay,
                    grossIncome,
                    netPay
            );

            Payroll saved = payrollRepository.save(payroll);

            saveDeductions(saved, sss, philhealth, pagibig, witholdingTax);
        }
    }

    private void saveDeductions(Payroll payroll, double sss, double philhealth, double pagibig, double witholdingTax) {
        Deductions sssDeduction = createDeduction(payroll, sss, "SSS");
        Deductions philhealthDeduction = createDeduction(payroll, philhealth, "PHIC");
        Deductions pagibigDeduction = createDeduction(payroll, pagibig, "HDMF");
        Deductions withholdingTaxDeduction = createDeduction(payroll, witholdingTax, "TAX");

        payroll.setDeductions(List.of(sssDeduction, philhealthDeduction, pagibigDeduction, withholdingTaxDeduction));

        payrollRepository.save(payroll);
    }

    private Deductions createDeduction(Payroll payroll, double amount, String deductionCode) {
        Deductions deduction = new Deductions();
        deduction.setPayroll(payroll);
        deduction.setAmount(Math.round(amount));

        DeductionType deductionType = new DeductionType();
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
