package com.motorph.ems.dto.mapper;


import com.motorph.ems.dto.*;
import com.motorph.ems.model.Department;
import com.motorph.ems.model.Employee;
import com.motorph.ems.model.Employee.Contact;
import com.motorph.ems.model.Employee.GovernmentId;
import com.motorph.ems.model.EmploymentStatus;
import com.motorph.ems.model.Position;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class EmployeeMapper {

    private final BenefitsMapper benefitsMapper;
    private final LeaveBalanceMapper leaveBalanceMapper;
    private final PositionMapper positionMapper;
    private final DepartmentMapper departmentMapper;
    private final EmploymentStatusMapper statusMapper;

    public EmployeeMapper(
            BenefitsMapper benefitsMapper,
            LeaveBalanceMapper leaveBalanceMapper,
            PositionMapper positionMapper,
            DepartmentMapper departmentMapper,
            EmploymentStatusMapper statusMapper) {
        this.benefitsMapper = benefitsMapper;
        this.leaveBalanceMapper = leaveBalanceMapper;
        this.positionMapper = positionMapper;
        this.departmentMapper = departmentMapper;
        this.statusMapper = statusMapper;
    }

    public EmployeeDTO toBasicDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        return EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .lastName(employee.getLastName())
                .firstName(employee.getFirstName())
                .position(positionMapper.toDTO(employee.getPosition()))
                .department(departmentMapper.toDTO(employee.getDepartment()))
                .build();
    }

    public EmployeeDTO toFullDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        return EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .lastName(employee.getLastName())
                .firstName(employee.getFirstName())
                .dob(employee.getDob())
                .address(employee.getAddress())
                .hireDate(employee.getHireDate())
                .basicSalary(employee.getBasicSalary())
                .semiMonthlyRate(employee.getSemiMonthlyRate())
                .hourlyRate(employee.getHourlyRate())

                // From DTOs
                .contacts(toContactDTO(employee.getContacts()))
                .position(positionMapper.toDTO(employee.getPosition()))
                .department(departmentMapper.toDTO(employee.getDepartment()))
                .governmentId(toGovernmentIdDTO(employee.getGovernmentId()))
                .supervisor(toSupervisorDTO(employee.getSupervisor()))
                .status(statusMapper.toDTO(employee.getStatus()))
                .build();
    }

    public SupervisorDTO toSupervisorDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        return SupervisorDTO.builder()
                .supervisorId(employee.getEmployeeId())
                .lastName(employee.getLastName())
                .firstName(employee.getFirstName())
                .address(employee.getAddress())
                .position(positionMapper.toDTO(employee.getPosition()))
                .contacts(toContactDTO(employee.getContacts()))
                .build();
    }

    public Employee toEntity(EmployeeDTO employeeFullDTO) {
        if (employeeFullDTO == null || employeeFullDTO.employeeId() == null) {
            return null;
        }

        return new Employee(
                employeeFullDTO.employeeId(),
                employeeFullDTO.lastName(),
                employeeFullDTO.firstName(),
                employeeFullDTO.dob(),
                employeeFullDTO.address(),
                employeeFullDTO.hireDate(),
                employeeFullDTO.basicSalary(),
                employeeFullDTO.supervisor().supervisorId(),
                employeeFullDTO.position().positionCode(),
                employeeFullDTO.department().departmentCode(),
                employeeFullDTO.status().statusId(),
                toContactEntity(employeeFullDTO.contacts()),
                toGovernmentIdEntity(employeeFullDTO.governmentId()),
                benefitsMapper.toEntity(employeeFullDTO.benefits()),
                leaveBalanceMapper.toEntity(employeeFullDTO.leaveBalances())
        );
    }

    public List<Employee> toEntity(List<EmployeeDTO> employeeFullDTO) {
        if (employeeFullDTO == null) {
            return null;
        }

        return employeeFullDTO.stream()
                .map(this::toEntity)
                .collect(toList());
    }

    public ContactDTO toContactDTO(List<Contact> contact) {
        if (contact == null) {
            return null;
        }

        return ContactDTO.builder()
                .employeeId(contact.getFirst().getEmployee().getEmployeeId())
                .contactNumbers(contact.stream()
                        .map(Contact::getContactNo)
                        .collect(toList()))
                .build();
    }

    public List<Contact> toContactEntity(ContactDTO contactDTO) {
        if (
                contactDTO == null ||
                contactDTO.contactNumbers() == null ||
                contactDTO.employeeId() == null) {
            return null;
        }

        return contactDTO.contactNumbers().stream()
                .map(number -> new Contact(contactDTO.employeeId(), number))
                .collect(toList());
    }

    public GovernmentIdDTO toGovernmentIdDTO (GovernmentId governmentId) {
        if (governmentId == null) {
            return null;
        }

        return GovernmentIdDTO.builder()
                .id(governmentId.getId())
                .employeeId(governmentId.getEmployee().getEmployeeId())
                .sssNo(governmentId.getSssNo())
                .philHealthNo(governmentId.getPhilHealthNo())
                .pagIbigNo(governmentId.getPagIbigNo())
                .tinNo(governmentId.getTinNo())
                .build();
    }

    public GovernmentId toGovernmentIdEntity(GovernmentIdDTO governmentIdDTO) {
        if (governmentIdDTO == null || governmentIdDTO.employeeId() == null) {
            return null;
        }

        return new GovernmentId(
                governmentIdDTO.employeeId(),
                governmentIdDTO.sssNo(),
                governmentIdDTO.philHealthNo(),
                governmentIdDTO.pagIbigNo(),
                governmentIdDTO.tinNo()
        );
    }

    public void updateEntity(EmployeeDTO employeeFullDTO, Employee employee) {
        if (employeeFullDTO == null || employee == null) {
            throw new IllegalArgumentException("Employee DTO or employee cannot be null");
        }

        if (employeeFullDTO.employeeId() != null && !employee.getEmployeeId().equals(employeeFullDTO.employeeId())) {
            throw new IllegalArgumentException("Employee ID cannot be changed");
        }

        employee.setLastName(employeeFullDTO.lastName());
        employee.setFirstName(employeeFullDTO.firstName());
        employee.setDob(employeeFullDTO.dob());
        employee.setHireDate(employeeFullDTO.hireDate());
        employee.setBasicSalary(employeeFullDTO.basicSalary());
        employee.setSemiMonthlyRate(employeeFullDTO.semiMonthlyRate());
        employee.setHourlyRate(employeeFullDTO.hourlyRate());
        employee.setPosition(new Position(employeeFullDTO.position().positionCode()));
        employee.setDepartment(new Department(employeeFullDTO.department().departmentCode()));
        employee.setStatus(new EmploymentStatus(employeeFullDTO.status().statusId()));
        employee.setSupervisor(new Employee(employeeFullDTO.supervisor().supervisorId()));
        employee.setContacts(toContactEntity(employeeFullDTO.contacts()));
        employee.setGovernmentId(toGovernmentIdEntity(employeeFullDTO.governmentId()));
        employee.setBenefits(benefitsMapper.toEntity(employeeFullDTO.benefits()));
        employee.setLeaveBalances(leaveBalanceMapper.toEntity(employeeFullDTO.leaveBalances()));
    }
}
