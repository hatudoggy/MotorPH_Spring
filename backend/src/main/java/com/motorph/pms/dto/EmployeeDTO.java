package com.motorph.pms.dto;

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
        List<ContactDTO> contacts,
        PositionDTO position,
        DepartmentDTO department,
        GovernmentIdDTO governmentId,
        SupervisorDTO supervisor,
        EmploymentStatusDTO status,
        LocalDate hireDate,
        Double basicSalary,
        Double semiMonthlyRate,
        Double hourlyRate,
        Double overtimeRate,
        List<BenefitDTO> benefits,
        List<LeaveBalanceDTO> leaveBalances
) {
}
