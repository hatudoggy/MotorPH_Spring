package com.motorph.pms.service.impl;

import com.motorph.pms.dto.EmployeeDTO;
import com.motorph.pms.dto.mapper.EmployeeMapper;
import com.motorph.pms.model.Employee;
import com.motorph.pms.repository.EmployeeRepository;
import com.motorph.pms.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = {"employeeList", "employee", "supervisors"})
@Slf4j
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

    @CacheEvict(cacheNames = {"employeeList", "supervisors"}, allEntries = true)
    @CachePut(cacheNames = {"employee"}, key = "#result.employeeId")
    @Override
    @Transactional
    public EmployeeDTO addNewEmployee(EmployeeDTO employeeFullDTO) {
        log.debug("Adding new employee: {}", employeeFullDTO);

        if (employeeRepository.existsByFirstNameAndLastName(employeeFullDTO.firstName(), employeeFullDTO.lastName())) {
            throw new EntityNotFoundException("Employee with first name " + employeeFullDTO.firstName() + " and last name " + employeeFullDTO.lastName() + " already exists");
        }

        Employee employee = employeeMapper.toEntity(employeeFullDTO);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toFullDTO(savedEmployee);
    }

    @Cacheable(cacheNames = "employeeList")
    @Override
    public List<EmployeeDTO> getEmployees() {
        log.debug("Fetching all employees");

        return employeeRepository.findAll().stream().map(employeeMapper::toLimitedDTO).toList();
    }

    @Cacheable(cacheNames = "employee", key = "#employeeID")
    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long employeeID, boolean isFullDetails) {
        if (isFullDetails) {
            log.debug("Fetching employee: {}", employeeID);
            return employeeRepository.findById(employeeID).map(employeeMapper::toFullDTO);
        } else {
            log.debug("Fetching limited employee: {}", employeeID);
            return employeeRepository.findById(employeeID).map(employeeMapper::toLimitedDTO);
        }
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = {"employeeList", "supervisors"}, allEntries = true),
            @CacheEvict(cacheNames = "employee", key = "#employeeID")
    })
    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long employeeID, EmployeeDTO employeeFullDTO) {
        log.debug("Updating employee: {}", employeeID);

        Employee employee = employeeRepository.findById(employeeID).orElseThrow(
                () -> new EntityNotFoundException("Employee: " + employeeFullDTO.employeeId() + " not found")
        );

        employeeMapper.updateEntity(employeeFullDTO, employee);

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeMapper.toFullDTO(updatedEmployee);
    }

    @CacheEvict(cacheNames = {"employeeList", "supervisors"}, allEntries = true)
    @Override
    @Transactional
    public void addNewEmployeesFromCSV(String employeeCSVPath) {
        log.debug("Adding new employees from CSV: {}", employeeCSVPath);

        //TODO: implement adding employees from CSV
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Cacheable(cacheNames = "employeeList", key = "#isActive + '_' + #isFullDetails")
    @Override
    public List<EmployeeDTO> findActiveEmployees(boolean isActive, boolean isFullDetails) {
        log.debug("Fetching active [{}] employees full[{}]", isActive, isFullDetails);
        List<Employee> employees;

        if (isActive) {
            employees = employeeRepository.findAllExceptByStatus_StatusIds(List.of(5,6));
        } else {
            employees = employeeRepository.findAllExceptByStatus_StatusIds(List.of(1,2,3));
        }

        if (isFullDetails){
            return employees.stream().map(employeeMapper::toFullDTO).toList();
        } else {
            return employees.stream().map(employeeMapper::toLimitedDTO).toList();
        }
    }
}
