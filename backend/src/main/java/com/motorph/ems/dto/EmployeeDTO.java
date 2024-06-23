package com.motorph.ems.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EmployeeDTO(
        Long employeeId,
        String lastName,
        String firstName,
        LocalDate dob,
        String address,
        ContactDTO contacts,
        String positionCode,
        String departmentCode,
        GovernmentIdDTO governmentId,
        Long supervisorId,
        int statusId,
        LocalDate hireDate,
        Double basicSalary,
        Double semiMonthlyRate,
        Double hourlyRate,
        List<BenefitDTO> benefits,
        List<LeaveBalanceDTO> leaveBalances
) {}
