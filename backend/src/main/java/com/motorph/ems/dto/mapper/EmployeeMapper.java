package com.motorph.ems.dto.mapper;


import com.motorph.ems.dto.ContactDTO;
import com.motorph.ems.dto.EmployeeDTO;
import com.motorph.ems.dto.GovernmentIdDTO;
import com.motorph.ems.dto.SupervisorDTO;
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

    public EmployeeMapper(
            BenefitsMapper benefitsMapper,
            LeaveBalanceMapper leaveBalanceMapper) {
        this.benefitsMapper = benefitsMapper;
        this.leaveBalanceMapper = leaveBalanceMapper;
    }

    public EmployeeDTO toDTO(Employee employee) {
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
                .positionCode(employee.getPosition().getPositionCode())
                .departmentCode(employee.getDepartment().getDepartmentCode())
                .governmentId(toGovernmentIdDTO(employee.getGovernmentId()))
                .supervisorId(employee.getSupervisor().getEmployeeId())
                .statusId(employee.getStatus().getStatusId())
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
                .positionCode(employee.getPosition().getPositionCode())
                .contacts(toContactDTO(employee.getContacts()))
                .build();
    }

    public Employee toEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null || employeeDTO.employeeId() == null) {
            return null;
        }

        return new Employee(
                employeeDTO.employeeId(),
                employeeDTO.lastName(),
                employeeDTO.firstName(),
                employeeDTO.dob(),
                employeeDTO.address(),
                employeeDTO.hireDate(),
                employeeDTO.basicSalary(),
                employeeDTO.supervisorId(),
                employeeDTO.positionCode(),
                employeeDTO.departmentCode(),
                employeeDTO.statusId(),
                toContactEntity(employeeDTO.contacts()),
                toGovernmentIdEntity(employeeDTO.governmentId()),
                benefitsMapper.toEntity(employeeDTO.benefits()),
                leaveBalanceMapper.toEntity(employeeDTO.leaveBalances())
        );
    }

    public List<EmployeeDTO> toDTO(List<Employee> employee) {
        if (employee == null) {
            return null;
        }

        return employee.stream()
                .map(this::toDTO)
                .collect(toList());
    }

    public List<Employee> toEntity(List<EmployeeDTO> employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }

        return employeeDTO.stream()
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

    public void updateEntity(EmployeeDTO employeeDTO, Employee employee) {
        if (employeeDTO == null || employee == null) {
            throw new IllegalArgumentException("Employee DTO or employee cannot be null");
        }

        if (employeeDTO.employeeId() != null && !employee.getEmployeeId().equals(employeeDTO.employeeId())) {
            throw new IllegalArgumentException("Employee ID cannot be changed");
        }

        employee.setLastName(employeeDTO.lastName());
        employee.setFirstName(employeeDTO.firstName());
        employee.setDob(employeeDTO.dob());
        employee.setHireDate(employeeDTO.hireDate());
        employee.setBasicSalary(employeeDTO.basicSalary());
        employee.setSemiMonthlyRate(employeeDTO.semiMonthlyRate());
        employee.setHourlyRate(employeeDTO.hourlyRate());
        employee.setPosition(new Position(employeeDTO.positionCode()));
        employee.setDepartment(new Department(employeeDTO.departmentCode()));
        employee.setStatus(new EmploymentStatus(employeeDTO.statusId()));
        employee.setSupervisor(new Employee(employeeDTO.supervisorId()));
        employee.setContacts(toContactEntity(employeeDTO.contacts()));
        employee.setGovernmentId(toGovernmentIdEntity(employeeDTO.governmentId()));
        employee.setBenefits(benefitsMapper.toEntity(employeeDTO.benefits()));
        employee.setLeaveBalances(leaveBalanceMapper.toEntity(employeeDTO.leaveBalances()));
    }
}
