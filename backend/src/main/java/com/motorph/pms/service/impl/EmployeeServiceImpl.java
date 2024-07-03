package com.motorph.pms.service.impl;

import com.motorph.pms.dto.EmployeeDTO;
import com.motorph.pms.dto.SupervisorDTO;
import com.motorph.pms.dto.mapper.EmployeeMapper;
import com.motorph.pms.model.Employee;
import com.motorph.pms.repository.EmployeeRepository;
import com.motorph.pms.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public EmployeeDTO addNewEmployee(EmployeeDTO employeeFullDTO) {
        if (employeeRepository.existsByFirstNameAndLastName(employeeFullDTO.firstName(), employeeFullDTO.lastName())) {
            throw new EntityNotFoundException("Employee with first name " + employeeFullDTO.firstName() + " and last name " + employeeFullDTO.lastName() + " already exists");
        }

        Employee employee = employeeMapper.toEntity(employeeFullDTO);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toFullDTO(savedEmployee);
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long employeeID, boolean isFullDetails) {
        if (isFullDetails) {
            return employeeRepository.findById(employeeID).map(employeeMapper::toFullDTO);
        }

        else {
            return employeeRepository.findById(employeeID).map(employeeMapper::toLimitedDTO);
        }
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeByName(String firstName, String lastName) {
        return employeeRepository.findByFirstNameAndLastName(firstName, lastName).map(employeeMapper::toFullDTO);
    }

    @Override
    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return Collections.emptyList();
        }

        return employees.stream()
                .map(employeeMapper::toLimitedDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(String departmentName) {
        return employeeRepository.findAllByDepartment_DepartmentName(departmentName)
                .stream().map(employeeMapper::toLimitedDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByPosition(String positionName) {
        return employeeRepository.findAllByPosition_PositionName(positionName)
                .stream().map(employeeMapper::toLimitedDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByStatus(String statusName) {
        return employeeRepository.findAllByStatus_StatusName(statusName)
                .stream().map(employeeMapper::toLimitedDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesBySupervisorId(Long supervisorId) {
        return employeeRepository.findAllBySupervisor_EmployeeId(supervisorId)
                .stream().map(employeeMapper::toLimitedDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesBySupervisorName(String firstName, String lastName) {
        return employeeRepository.findAllBySupervisor_FirstName_AndSupervisor_LastName(firstName, lastName)
                .stream().map(employeeMapper::toLimitedDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByHiredBetween(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findAllByHireDateBetween(startDate, endDate)
                .stream().map(employeeMapper::toLimitedDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long employeeID, EmployeeDTO employeeFullDTO) {
        Employee employee = employeeRepository.findById(employeeID).orElseThrow(
                () -> new RuntimeException("Employee: " + employeeFullDTO.employeeId() + " not found")
        );

        System.out.println(
                "Employee: " + employee.getEmployeeId() +
                        ", First Name: " + employee.getFirstName() +
                        ", Last Name: " + employee.getLastName() +
                        ", Address: " + employee.getAddress()
        );

        employeeMapper.updateEntity(employeeFullDTO, employee);

        System.out.println(
                "Employee: " + employee.getEmployeeId() +
                        ", First Name: " + employee.getFirstName() +
                        ", Last Name: " + employee.getLastName() +
                        ", Address: " + employee.getAddress()
        );


        return employeeMapper.toFullDTO(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeeID) {
        Employee employee = employeeRepository.findById(employeeID).orElseThrow(
                () -> new RuntimeException("EmployeeDTO not found with status: " + employeeID)
        );

        employeeRepository.delete(employee);
    }

    @Override
    public void addNewEmployeesFromCSV(String employeeCSVPath) {
        //TODO: implement adding employees from CSV
    }

    @Override
    public List<SupervisorDTO> getSupervisors() {
        return employeeRepository.findAllByPosition_isLeader(true).stream()
                .map(employeeMapper::toSupervisorDTO).toList();
    }
}
