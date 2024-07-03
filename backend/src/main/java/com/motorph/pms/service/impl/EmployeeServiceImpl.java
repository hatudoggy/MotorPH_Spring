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

import java.util.List;
import java.util.Optional;

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
    public List<EmployeeDTO> getEmployees() {
        return employeeRepository.findAll().stream().map(employeeMapper::toLimitedDTO).toList();
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
    public void addNewEmployeesFromCSV(String employeeCSVPath) {
        //TODO: implement adding employees from CSV
    }

    @Override
    public List<SupervisorDTO> getSupervisors() {
        return employeeRepository.findAllByPosition_isLeader(true).stream()
                .map(employeeMapper::toSupervisorDTO).toList();
    }
}
