package com.motorph.ems.service.impl;

import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.dto.mapper.PayrollMapper;
import com.motorph.ems.model.Payroll;
import com.motorph.ems.repository.PayrollRepository;
import com.motorph.ems.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final PayrollMapper payrollMapper;

    @Autowired
    public PayrollServiceImpl(PayrollRepository payrollRepository, PayrollMapper payrollMapper) {
        this.payrollRepository = payrollRepository;
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
    public Optional<PayrollDTO> getPayrollById(Long payrollId) {
        return payrollRepository.findById(payrollId).map(payrollMapper::toDTO);
    }

    @Override
    public Optional<PayrollDTO> getPayrollByEmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart) {
        return payrollRepository.findByEmployee_EmployeeIdAndPeriodStart(employeeId, periodStart)
                .map(payrollMapper::toDTO);
    }

    @Override
    public PayrollDTO updatePayroll(PayrollDTO payrollDTO) {
        Payroll existingPayroll = payrollRepository.findById(payrollDTO.payrollId()).orElseThrow(
                () -> new IllegalStateException("Payroll with payrollId " + payrollDTO.payrollId() + " does not exist"
        ));

        payrollMapper.updateEntity(payrollDTO, existingPayroll);

        return payrollMapper.toDTO(payrollRepository.save(existingPayroll));
    }

    @Override
    public void deletePayroll(Long payrollId) {
        if (!payrollRepository.existsById(payrollId)) {
            throw new IllegalStateException("Payroll with payrollId " + payrollId + " does not exist");
        }

        payrollRepository.deleteById(payrollId);
    }

    @Override
    public List<PayrollDTO> getPayrollsByEmployeeId(Long employeeId) {
        return payrollRepository.findAllByEmployee_EmployeeId(employeeId).stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PayrollDTO> getPayrollsForPeriod(LocalDate periodStart, LocalDate periodEnd) {
        return payrollRepository.findAllByPeriodStartAndPeriodEnd(periodStart, periodEnd).stream()
                .map(payrollMapper::toDTO)
                .collect(Collectors.toList());
    }
}
