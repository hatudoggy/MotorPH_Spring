package com.motorph.pms.service.impl;

import com.motorph.pms.dto.MonthlyPayrollReportDTO;
import com.motorph.pms.dto.PayrollDTO;
import com.motorph.pms.dto.mapper.PayrollMapper;
import com.motorph.pms.model.*;
import com.motorph.pms.model.Deductions.DeductionType;
import com.motorph.pms.repository.AttendanceRepository;
import com.motorph.pms.repository.EmployeeRepository;
import com.motorph.pms.repository.PayrollRepository;
import com.motorph.pms.service.MatrixService;
import com.motorph.pms.service.PayrollService;
import com.motorph.pms.util.PayrollCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayrollMapper payrollMapper;
    private final PayrollCalculator payrollCalculator;

    @Autowired
    public PayrollServiceImpl(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository,
            PayrollMapper payrollMapper,
            PayrollCalculator payrollCalculator) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.payrollMapper = payrollMapper;
        this.payrollCalculator = payrollCalculator;
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

    @Transactional
    @Override
    public int batchGeneratePayroll(LocalDate periodStart, LocalDate periodEnd) {
        List<Employee> employees = employeeRepository.findAllExceptByStatus_StatusIds(List.of(4,5,6));

        int count = 0;

        for (Employee employee : employees) {

            List<Attendance> attendances = attendanceRepository.findAllByEmployee_EmployeeId_AndDateBetween(
                    employee.getEmployeeId(), periodStart, periodEnd
            );

            int presentCount = payrollCalculator.countPresent(attendances);
            double hoursWorked = payrollCalculator.calculateHoursWorked(attendances);
            double overtimeHours = payrollCalculator.calculateOvertimeHours(attendances);
            double monthlyRate = employee.getBasicSalary();
            double hourlyRate = employee.getHourlyRate();
            double overtimeRate = employee.getOvertimeRate();

            double grossIncome = payrollCalculator.calculateGrossIncome(hoursWorked,hourlyRate,overtimeHours,overtimeRate);

            if (grossIncome <= 0) {
                continue;
            }

            double sss = payrollCalculator.calculateSSS(grossIncome);

            double philhealth = payrollCalculator.calculatePhilHealth(grossIncome);

            double pagibig = payrollCalculator.calculatePagIbig(grossIncome);

            double withholdingTax = payrollCalculator.calculateTax(grossIncome,List.of(sss,philhealth,pagibig));

            double totalDeduction = payrollCalculator.calculateTotalDeductions(List.of(sss, philhealth, pagibig, withholdingTax));

            double totalBenefits = payrollCalculator.calculateTotalBenefits(employee.getBenefits().stream().map(Benefits::getAmount).collect(Collectors.toList()));

            double netPay = payrollCalculator.calculateNetPay(grossIncome, totalBenefits, totalBenefits);


            Payroll payroll = new Payroll(
                    employee.getEmployeeId(),
                    periodStart,
                    periodEnd,
                    presentCount,
                    monthlyRate,
                    hourlyRate,
                    hoursWorked,
                    overtimeHours,
                    overtimeRate,
                    grossIncome,
                    totalBenefits,
                    totalDeduction,
                    netPay
            );

            List<Deductions> deductions = List.of(
                    new Deductions(payroll, "SSS", sss),
                    new Deductions(payroll, "PHIC", philhealth),
                    new Deductions(payroll, "HDMF", pagibig),
                    new Deductions(payroll, "TAX", withholdingTax)
            );

            payroll.setDeductions(deductions);

            payrollRepository.save(payroll);

            count++;
        }

        return count;
    }

    @Override
    public PayrollDTO generatePayroll(String startDate, String endDate, Long employeeId) {
        //TODO: implement this
        return null;
    }
}
