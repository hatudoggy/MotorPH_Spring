package com.motorph.ems.service;

import com.motorph.ems.model.Employee;

import java.util.List;


public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(long id);
    void createEmployee(Employee employee);
    void updateEmployee(Employee employee);
    void deleteEmployee(long id);
}
