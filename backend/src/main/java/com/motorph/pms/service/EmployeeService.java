package com.motorph.ems.service;

import com.motorph.pms.dto.EmployeeDTO;
import com.motorph.pms.dto.SupervisorDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    EmployeeDTO addNewEmployee(EmployeeDTO employee);

    Optional<EmployeeDTO> getEmployeeById(Long employeeId, boolean isFullDetails);

    Optional<EmployeeDTO> getEmployeeByName(String firstName, String lastName);

    List<EmployeeDTO> getEmployees();

    List<EmployeeDTO> getEmployeesByDepartment(String departmentName);

    List<EmployeeDTO> getEmployeesByPosition(String positionName);

    List<EmployeeDTO> getEmployeesByStatus(String statusName);

    List<EmployeeDTO> getEmployeesBySupervisorId(Long supervisorId);

    List<EmployeeDTO> getEmployeesBySupervisorName(String firstName, String lastName);

    List<EmployeeDTO> getEmployeesByHiredBetween(LocalDate startDate, LocalDate endDate);

    EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employee);

    void deleteEmployee(Long employeeId);

    void addNewEmployeesFromCSV(String employeeCSVPath);

    List<SupervisorDTO> getSupervisors();
}
