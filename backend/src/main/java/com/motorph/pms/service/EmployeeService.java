package com.motorph.pms.service;

import com.motorph.pms.dto.EmployeeDTO;
import com.motorph.pms.dto.SupervisorDTO;
import com.motorph.pms.model.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    EmployeeDTO addNewEmployee(EmployeeDTO employee);

    List<EmployeeDTO> getEmployees();

    Optional<EmployeeDTO> getEmployeeById(Long employeeId, boolean isFullDetails);

    EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employee);

    void addNewEmployeesFromCSV(String employeeCSVPath);

    List<EmployeeDTO> findActiveEmployees(boolean isActive, boolean isFullDetails);
}
