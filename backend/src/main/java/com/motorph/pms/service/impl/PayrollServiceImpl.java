package com.motorph.pms.service.impl;

import com.motorph.pms.dto.BenefitDTO;
import com.motorph.pms.dto.EmployeeDTO;
import com.motorph.pms.dto.PayrollDTO;
import com.motorph.pms.dto.mapper.PayrollMapper;
import com.motorph.pms.model.*;
import com.motorph.pms.repository.AttendanceRepository;
import com.motorph.pms.repository.PayrollRepository;
import com.motorph.pms.service.EmployeeService;
import com.motorph.pms.service.PayrollService;
import com.motorph.pms.util.PayrollCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = {"payrollList", "payroll"})
@Slf4j
@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayrollMapper payrollMapper;
    private final PayrollCalculator payrollCalculator;

    private final EmployeeService employeeService;

    @Autowired
    public PayrollServiceImpl(
            PayrollRepository payrollRepository,
            EmployeeService employeeService,
            AttendanceRepository attendanceRepository,
            PayrollMapper payrollMapper,
            PayrollCalculator payrollCalculator) {
        this.payrollRepository = payrollRepository;
        this.attendanceRepository = attendanceRepository;
        this.payrollMapper = payrollMapper;
        this.payrollCalculator = payrollCalculator;
        this.employeeService = employeeService;
    }

    @CachePut(cacheNames = "payroll", key = "#result.payrollId()")
    @CacheEvict(cacheNames = "payrollList", allEntries = true)
    @Transactional
    @Override
    public PayrollDTO addNewPayroll(PayrollDTO payrollDTO) {
        log.debug("Adding new payroll {}", payrollDTO);

        if (payrollRepository.existsByEmployee_EmployeeIdAndPeriodStart(
                payrollDTO.employee().employeeId(), payrollDTO.periodStart())) {
            throw new IllegalStateException("Payroll with employee " + payrollDTO.employee().employeeId() +
                    " and period start " + payrollDTO.periodStart() + " already exists");
        }

        Payroll payroll = payrollMapper.toEntity(payrollDTO);

        return payrollMapper.toDTO(payrollRepository.save(payroll));
    }

    @Cacheable(cacheNames = "payrollList", key = "#isFullDetails")
    @Override
    public List<PayrollDTO> getAllPayrolls(boolean isFullDetails) {
        log.debug("Fetching all payrolls");

        List<Payroll> payrolls = payrollRepository.findAll();

        return payrolls.stream()
                .map(isFullDetails ? payrollMapper::toDTO : payrollMapper::toLimitedDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "payrollList", key = "#employeeId")
    @Override
    public List<PayrollDTO> getPayrollsByEmployeeId(Long employeeId) {
        log.debug("Fetching all payrolls for employee {}", employeeId);

        return payrollRepository.findAllByEmployee_EmployeeId(employeeId)
                .stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "payrollList", key = "#isFullDetails + '_' + #periodStart + '_' + #periodEnd")
    @Override
    public List<PayrollDTO> getPayrollsForPeriod(boolean isFullDetails, LocalDate periodStart, LocalDate periodEnd) {
        log.debug("Fetching all payrolls for period {} - {}", periodStart, periodEnd);

        List<Payroll> payrolls = payrollRepository.findAllByPeriodStartAndPeriodEnd(periodStart, periodEnd);

        return payrolls.stream()
                .map(isFullDetails ? payrollMapper::toDTO : payrollMapper::toLimitedDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "payrollList", key = "#employeeId + '_' + #start + '_' + #end")
    @Override
    public List<PayrollDTO> getPayrollByEmployeeIdAndPeriodRange(long employeeId, LocalDate start, LocalDate end) {
        log.debug("Fetching all payrolls for employee {} for period {} - {}", employeeId, start, end);

        return payrollRepository.findAllByEmployeeEmployeeIdAndPeriodStartAndPeriodEnd(employeeId, start, end)
                .stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "payrollList", key = "#isFullDetails + '_' + #isStartDate + '_' + #date")
    @Override
    public List<PayrollDTO> getPayrollsByDate(boolean isFullDetails, boolean isStartDate, LocalDate date) {
        log.debug("Fetching all payrolls for date {}", date);

        List<Payroll> payrolls = isStartDate ? payrollRepository.findAllByPeriodStart(date)
                : payrollRepository.findAllByPeriodEnd(date);

        return payrolls.stream()
                .map(isFullDetails ? payrollMapper::toDTO : payrollMapper::toLimitedDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "payroll", key = "#payrollId")
    @Override
    public Optional<PayrollDTO> getPayrollById(Long payrollId) {
        log.debug("Fetching payroll {}", payrollId);

        return payrollRepository.findById(payrollId).map(payrollMapper::toDTO);
    }

    @CachePut(cacheNames = "payroll", key = "#payrollId")
    @Transactional
    @Override
    public PayrollDTO updatePayroll(Long payrollId, PayrollDTO payrollDTO) {
        log.debug("Updating payroll {}", payrollId);

        Payroll existingPayroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new IllegalStateException("Payroll with payrollId " + payrollDTO.payrollId() + " does not exist"));

        payrollMapper.updateEntity(payrollDTO, existingPayroll);

        return payrollMapper.toDTO(payrollRepository.save(existingPayroll));
    }

    @Cacheable(cacheNames = "payrollList")
    @Override
    public List<LocalDate> getDistinctMonthsByYear(int year) {
        log.debug("Fetching distinct months for year {}", year);

        return payrollRepository.findDistinctMonthsByYear(year);
    }

    @Cacheable(cacheNames = "payrollList")
    @Override
    public List<Integer> getDistinctYears() {
        log.debug("Fetching distinct years");

        return payrollRepository.findDistinctYears();
    }

    @CacheEvict(cacheNames = {"payroll", "payrollList"}, key = "#payrollId", allEntries = true)
    @Transactional
    @Override
    public void deletePayroll(Long payrollId) {
        log.debug("Deleting payroll {}", payrollId);

        if (!payrollRepository.existsById(payrollId)) {
            throw new IllegalStateException("Payroll with payrollId " + payrollId + " does not exist");
        }

        payrollRepository.deleteById(payrollId);
    }

//    @Cacheable
//    @Override
//    public List<MonthlyPayrollReportDTO> getMonthlyReport(LocalDate start, LocalDate end) {
////        List<Object[]> results = payrollRepository.getTotalEarningsAndDeductionsByMonth(start, end);
////        List<MonthlyPayrollReportDTO> dtoList = new ArrayList<>();
////        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
////
////        for (Object[] result : results) {
////            LocalDate monthYear = LocalDate.parse((String) result[0]);
////            double totalEarnings = (Double) result[1];
////            double totalDeductions = (Double) result[2];
////
////            MonthlyPayrollReportDTO dto = new MonthlyPayrollReportDTO(monthYear, totalEarnings, totalDeductions);
////            dtoList.add(dto);
////        }
////
////        return dtoList;
//        return null;
//    }

    @CacheEvict(cacheNames = "payrollList", allEntries = true)
    @Transactional
    @Override
    public int batchGeneratePayroll(LocalDate periodStart, LocalDate periodEnd) {
        log.debug("Batch generating payroll for period {} - {}", periodStart, periodEnd);

        int count = 0;

        List<EmployeeDTO> employees = employeeService.findActiveEmployees(true, true);

        for (EmployeeDTO employee : employees) {

            List<Attendance> attendances = attendanceRepository.findAllByEmployee_EmployeeId_AndDateBetween(
                    employee.employeeId(), periodStart, periodEnd
            );

            int presentCount = payrollCalculator.countPresent(attendances);
            double hoursWorked = payrollCalculator.calculateHoursWorked(attendances);
            double overtimeHours = payrollCalculator.calculateOvertimeHours(attendances);
            double monthlyRate = employee.basicSalary();
            double hourlyRate = employee.hourlyRate();
            double overtimeRate = employee.overtimeRate();

            double grossIncome = payrollCalculator.calculateGrossIncome(hoursWorked,hourlyRate,overtimeHours,overtimeRate);

            if (grossIncome <= 0) {
                continue;
            }

            double sss = payrollCalculator.calculateSSS(grossIncome);

            double philhealth = payrollCalculator.calculatePhilHealth(grossIncome);

            double pagibig = payrollCalculator.calculatePagIbig(grossIncome);

            double withholdingTax = payrollCalculator.calculateTax(grossIncome,List.of(sss,philhealth,pagibig));

            double totalDeduction = payrollCalculator.calculateTotalDeductions(List.of(sss, philhealth, pagibig, withholdingTax));

            double totalBenefits = payrollCalculator.calculateTotalBenefits(employee.benefits().stream().map(BenefitDTO::amount).collect(Collectors.toList()));

            double netPay = payrollCalculator.calculateNetPay(grossIncome, totalBenefits, totalBenefits);


            Payroll payroll = new Payroll(
                    employee.employeeId(),
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


    @Transactional
    @Override
    public PayrollDTO generatePayroll(String startDate, String endDate, Long employeeId) {
        //TODO: implement this

        return null;
    }
}
