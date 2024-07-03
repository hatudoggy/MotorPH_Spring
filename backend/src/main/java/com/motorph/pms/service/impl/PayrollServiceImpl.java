package com.motorph.ems.service.impl;

import com.motorph.ems.dto.MonthlyPayrollReportDTO;
import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.dto.mapper.PayrollMapper;
import com.motorph.ems.model.*;
import com.motorph.ems.model.Deductions.DeductionType;
import com.motorph.ems.repository.AttendanceRepository;
import com.motorph.ems.repository.EmployeeRepository;
import com.motorph.ems.repository.PayrollRepository;
import com.motorph.ems.service.MatrixService;
import com.motorph.ems.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.List;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final MatrixService matrixService;
    private final PayrollMapper payrollMapper;

    @Autowired
    public PayrollServiceImpl(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository,
            MatrixService matrixService,
            PayrollMapper payrollMapper) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.matrixService = matrixService;
        this.payrollMapper = payrollMapper;
    }

    @Override
    public PayrollDTO addNewPayroll(PayrollDTO payrollDTO) {
        if (payrollRepository.existsByEmployee_EmployeeIdAndPeriodStart(
                payrollDTO.employee().employeeId(), payrollDTO.periodStart())) {
            throw new IllegalStateException("Payroll with employee " + payrollDTO.employee().employeeId() +
                    " and period start " + payrollDTO.periodStart() + " already exists");
        }

        Payroll payroll = payrollMapper.toEntity(payrollDTO);
        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Override
    public List<PayrollDTO> getAllPayrolls(boolean isFullDetails) {
        List<Payroll> payrolls = payrollRepository.findAll();

        if (isFullDetails){
            return payrolls.stream()
                    .map(payrollMapper::toDTO)
                    .collect(Collectors.toList());
        }
        else {
            return payrolls.stream()
                    .map(payrollMapper::toLimitedDTO)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<PayrollDTO> getPayrollsByEmployeeId(Long employeeId) {
        return payrollRepository.findAllByEmployee_EmployeeId(employeeId)
                .stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PayrollDTO> getPayrollsForPeriod(boolean isFullDetails, LocalDate periodStart, LocalDate periodEnd) {
        List<Payroll> payrolls = payrollRepository.findAllByPeriodStartAndPeriodEnd(periodStart, periodEnd);

        if (isFullDetails){
            return payrolls.stream()
                    .map(payrollMapper::toDTO)
                    .collect(Collectors.toList());
        }
        else {
            return payrolls.stream()
                    .map(payrollMapper::toLimitedDTO)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<PayrollDTO> getPayrollByEmployeeIdAndPeriodRange(long id, LocalDate start, LocalDate end) {
        return payrollRepository.findAllByEmployeeEmployeeIdAndPeriodStartAndPeriodEnd(id, start, end)
                .stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PayrollDTO> getPayrollsByDate(boolean isFullDetails, boolean isStartDate, LocalDate date) {
        List<Payroll> payrolls;

        if (isStartDate){
            payrolls = payrollRepository.findAllByPeriodStart(date);
        }
        else {
            payrolls = payrollRepository.findAllByPeriodEnd(date);
        }

        if (isFullDetails){
            return payrolls.stream()
                    .map(payrollMapper::toDTO)
                    .collect(Collectors.toList());
        }
        else {
            return payrolls.stream()
                    .map(payrollMapper::toLimitedDTO)
                    .collect(Collectors.toList());
        }
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
//        List<Object[]> results = payrollRepository.getTotalEarningsAndDeductionsByMonth(start, end);
//        List<MonthlyPayrollReportDTO> dtoList = new ArrayList<>();
//        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
//
//        for (Object[] result : results) {
//            LocalDate monthYear = LocalDate.parse((String) result[0]);
//            double totalEarnings = (Double) result[1];
//            double totalDeductions = (Double) result[2];
//
//            MonthlyPayrollReportDTO dto = new MonthlyPayrollReportDTO(monthYear, totalEarnings, totalDeductions);
//            dtoList.add(dto);
//        }
//
//        return dtoList;
        return null;
    }

    @Override
    public void batchGeneratePayroll(LocalDate periodStart, LocalDate periodEnd) {
        List<Employee> employees = employeeRepository.findAllExceptByStatus_StatusId(3);

        for (Employee employee : employees) {

            List<Attendance> attendances = attendanceRepository.findAllByEmployee_EmployeeId_AndDateBetween(
                    employee.getEmployeeId(), periodStart, periodEnd
            );

            int presentCount = countPresent(attendances);
            double hoursWorked = calculateHoursWorked(attendances);
            double overtimeHours = calculateOvertimeHours(attendances);
            double monthlyRate = employee.getBasicSalary();
            double hourlyRate = employee.getHourlyRate();
            double overtimeRate = employee.getOvertimeRate();

            Payroll payroll = new Payroll(
                    employee.getEmployeeId(),
                    periodStart,
                    periodEnd,
                    presentCount,
                    monthlyRate,
                    hourlyRate,
                    hoursWorked,
                    overtimeHours,
                    overtimeRate
            );

            double grossIncome = calculateGrossIncome(hoursWorked,hourlyRate,overtimeHours,overtimeRate);

            PhilhealthMatrix phm = matrixService.getPhilhealthMatrix(grossIncome);
            PagibigMatrix pgm = matrixService.getPagibigMatrix(grossIncome);

            double sss = matrixService.getSSSMatrix(grossIncome).getContribution();
            double philhealth = Math.floor(
                    (phm.getMonthlyPremiumCap() == null ?
                            phm.getMonthlyPremiumBase() :
                            grossIncome * phm.getPremiumRate()
                            ) / 2 * 100) / 100;

            double pagibig = Math.max((grossIncome * pgm.getTotalRate()), 100);
            double partialDeduction = sss + philhealth + pagibig;

            WitholdingTaxMatrix tm = matrixService.getWitholdingTaxMatrix(grossIncome - partialDeduction);

            double withholdingTax = (tm.getTaxBase() + (grossIncome - partialDeduction) - tm.getMinRange()) * tm.getTaxRate();

            double totalDeduction = calculateTotalDeductions(List.of(sss, philhealth, pagibig, withholdingTax));

            double totalBenefits = calculateTotalBenefits(employee.getBenefits().stream().map(Benefits::getAmount).collect(Collectors.toList()));

            double netPay = calculateNetPay(grossIncome,totalBenefits,totalBenefits);

            payroll.setGrossIncome(grossIncome);
            payroll.setTotalBenefits(totalBenefits);
            payroll.setTotalDeductions(totalDeduction);
            payroll.setNetPay(netPay);

            Payroll saved = payrollRepository.save(payroll);

            saveDeductions(saved, sss, philhealth, pagibig, withholdingTax);
        }
    }

    private void saveDeductions(Payroll payroll, double sss, double philhealth, double pagibig, double withholdingTax) {
        Deductions sssDeduction = createDeduction(payroll, sss, "SSS");
        Deductions philhealthDeduction = createDeduction(payroll, philhealth, "PHIC");
        Deductions pagibigDeduction = createDeduction(payroll, pagibig, "HDMF");
        Deductions withholdingTaxDeduction = createDeduction(payroll, withholdingTax, "TAX");

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


    private double calculateHoursWorked(List<Attendance> attendances) {
        return attendances.stream().mapToDouble(Attendance::calculateTotalHours).sum();
    }

    private double calculateOvertimeHours(List<Attendance> attendances) {
        return attendances.stream().mapToDouble(Attendance::calculateOvertime).sum();
    }


    private int countPresent(List<Attendance> attendances) {
        return attendances.size();
    }

    private double calculateGrossIncome(double hoursWorked, double hourlyRate, double overtimeHours, double overtimeRate) {
        if (overtimeHours > 0) {
            hoursWorked -= overtimeHours;
            return Math.round((hoursWorked * hourlyRate) + (overtimeHours * overtimeRate) * 100) / 100.0;
        }

        return Math.round(hoursWorked * hourlyRate * 100) / 100.0;
    }

    private double calculateTotalDeductions(List<Double> deductions) {
        return Math.round(deductions.stream().reduce(0.0, Double::sum) * 100) / 100.0;
    }

    private double calculateTotalBenefits(List<Double> benefits) {
        return Math.round(benefits.stream().reduce(0.0, Double::sum) * 100) / 100.0;
    }

    private double calculateGross(double hoursWorked, double hourlyRate, double overtimeHours, double overtimePay) {
        double gross = hoursWorked * hourlyRate;
        if (overtimeHours > 0){
            gross += overtimePay;
        }
        return Math.round(gross * 100) / 100.0; // Round the gross;
    }

    private double calculateNetPay(double grossIncome, double totalDeductions, double totalBenefits) {
        return (double) Math.round((grossIncome - totalDeductions + totalBenefits) * 100) / 100;
    }
}
