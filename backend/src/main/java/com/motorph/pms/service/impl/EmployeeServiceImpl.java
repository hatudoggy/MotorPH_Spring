package com.motorph.pms.service.impl;

import com.motorph.pms.dto.EmployeeDTO;
import com.motorph.pms.dto.SupervisorDTO;
import com.motorph.pms.dto.mapper.EmployeeMapper;
import com.motorph.pms.event.EmployeeChangedEvent;
import com.motorph.pms.model.Employee;
import com.motorph.pms.repository.EmployeeRepository;
import com.motorph.pms.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = {"employeeList", "employee", "supervisors"})
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            ApplicationEventPublisher eventPublisher) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.eventPublisher = eventPublisher;
    }

    @CacheEvict(cacheNames = {"employeeList", "supervisors"}, allEntries = true)
    @CachePut(cacheNames = {"employee"}, key = "#result.employeeId")
    @Override
    @Transactional
    public EmployeeDTO addNewEmployee(EmployeeDTO employeeFullDTO) {
        if (employeeRepository.existsByFirstNameAndLastName(employeeFullDTO.firstName(), employeeFullDTO.lastName())) {
            throw new EntityNotFoundException("Employee with first name " + employeeFullDTO.firstName() + " and last name " + employeeFullDTO.lastName() + " already exists");
        }

        Employee employee = employeeMapper.toEntity(employeeFullDTO);

        Employee savedEmployee = employeeRepository.save(employee);

        eventPublisher.publishEvent(new EmployeeChangedEvent(this, savedEmployee.getEmployeeId(), true));

        return employeeMapper.toFullDTO(savedEmployee);
    }

    @Cacheable(cacheNames = "employeeList")
    @Override
    public List<EmployeeDTO> getEmployees() {
        return employeeRepository.findAll().stream().map(employeeMapper::toLimitedDTO).toList();
    }

    @Cacheable(cacheNames = "employee", key = "#employeeID")
    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long employeeID, boolean isFullDetails) {
        if (isFullDetails) {
            return employeeRepository.findById(employeeID).map(employeeMapper::toFullDTO);
        } else {
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
        Employee employee = employeeRepository.findById(employeeID).orElseThrow(
                () -> new EntityNotFoundException("Employee: " + employeeFullDTO.employeeId() + " not found")
        );

        employeeMapper.updateEntity(employeeFullDTO, employee);

        Employee updatedEmployee = employeeRepository.save(employee);

        eventPublisher.publishEvent(new EmployeeChangedEvent(this, updatedEmployee.getEmployeeId(), true));

        return employeeMapper.toFullDTO(updatedEmployee);
    }

    @CacheEvict(cacheNames = {"employeeList", "supervisors"}, allEntries = true)
    @Override
    @Transactional
    public void addNewEmployeesFromCSV(String employeeCSVPath) {
        //TODO: implement adding employees from CSV
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Cacheable(cacheNames = "employeeList", key = "#isActive + '_' + #isFullDetails")
    @Override
    public List<EmployeeDTO> findActiveEmployees(boolean isActive, boolean isFullDetails) {
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
