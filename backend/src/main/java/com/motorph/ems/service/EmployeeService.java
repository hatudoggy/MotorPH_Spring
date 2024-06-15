package com.motorph.ems.service;

import com.motorph.ems.model.Employee;

import java.util.List;

/**
 * Represents a service for handling employee data.
 */
@SuppressWarnings("unused")
public interface EmployeeService {

    /**
     * Add a new employee record.
     *
     * @param Employee The employee record to add.
     */
    void addNewEmployee(Employee Employee);

    /**
     * Retrieves an employee record by employee ID.
     *
     * @param employeeID The ID of the employee.
     * @return The employee record.
     */
    Employee getEmployeeById(Long employeeID);

    /**
     * Retrieves a list of all employees.
     *
     * @return A list of all employee records.
     */
    List<Employee> getAllEmployees();

    /**
     * Update an existing employee record.
     *
     * @param employee The updated employee details.
     */
    Employee updateEmployee(Employee employee);

    /**
     * Delete an employee record.
     *
     * @param employeeID The Id of the employee to delete.
     */
    void deleteEmployee(Long employeeID);

    /**
     * Add employee records from a CSV file.
     *
     * @param employeeCSVPath The path to the CSV file containing employee records.
     */
    void addNewEmployeesFromCSV(String employeeCSVPath);

}