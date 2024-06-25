package com.motorph.ems.dto;

import lombok.Builder;
import lombok.Setter;

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
        PositionDTO position,
        DepartmentDTO department,
        GovernmentIdDTO governmentId,
        SupervisorDTO supervisor,
        EmploymentStatusDTO status,
        LocalDate hireDate,
        Double basicSalary,
        Double semiMonthlyRate,
        Double hourlyRate,
        List<BenefitDTO> benefits,
        List<LeaveBalanceDTO> leaveBalances
) {
}
