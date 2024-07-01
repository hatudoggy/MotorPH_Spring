package com.motorph.ems.dto.mapper;


import com.motorph.ems.dto.*;
import com.motorph.ems.model.*;
import com.motorph.ems.model.Employee.Contact;
import com.motorph.ems.model.Employee.GovernmentId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public EmployeeDTO toLimitedDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        return EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .lastName(employee.getLastName())
                .firstName(employee.getFirstName())
                .position(positionMapper.toDTO(employee.getPosition() == null ? null : employee.getPosition()))
                .department(departmentMapper.toDTO(employee.getDepartment() == null ? null : employee.getDepartment()))
                .hireDate(employee.getHireDate())
                .status(statusMapper.toDTO(employee.getStatus() == null ? null : employee.getStatus()))
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
                .leaveBalances(leaveBalanceMapper.toDTO(employee.getLeaveBalances()))
                .benefits(benefitsMapper.toDTO(employee.getBenefits()))
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

    private List<ContactDTO> toContactDTO(List<Contact> contacts) {
        if (contacts == null) return null;
        return contacts.stream()
                .map(contact -> ContactDTO.builder()
                        .contactId(contact.getContactId())
                        .employeeId(contact.getEmployee().getEmployeeId())
                        .contactNo(contact.getContactNo())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Contact> toContactEntity(List<ContactDTO> contactDTOs) {
        if (contactDTOs == null) return null;
        return contactDTOs.stream()
                .map(contactDTO -> new Contact(
                        contactDTO.contactId(),
                        new Employee(contactDTO.employeeId()),
                        contactDTO.contactNo()
                ))
                .collect(Collectors.toList());
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
        validateArguments(employeeFullDTO,employee);
        validateEmployeeId(employeeFullDTO,employee);

        // Update simple properties
        if (employeeFullDTO.lastName() != null) employee.setLastName(employeeFullDTO.lastName());
        if (employeeFullDTO.firstName() != null) employee.setFirstName(employeeFullDTO.firstName());
        if (employeeFullDTO.dob() != null) employee.setDob(employeeFullDTO.dob());
        if (employeeFullDTO.address() != null) employee.setAddress(employeeFullDTO.address());
        if (employeeFullDTO.hireDate() != null) employee.setHireDate(employeeFullDTO.hireDate());
        if (employeeFullDTO.basicSalary() != null) employee.setBasicSalary(employeeFullDTO.basicSalary());

        // Manually update foreign key relationships if provided
        if (employeeFullDTO.supervisor() != null) {
            Employee supervisor = new Employee(employeeFullDTO.supervisor().supervisorId());
            employee.setSupervisor(supervisor);
        }

        if (employeeFullDTO.position() != null) {
            Position position = new Position(employeeFullDTO.position().positionCode());
            employee.setPosition(position);
        }

        if (employeeFullDTO.department() != null) {
            Department department = new Department(employeeFullDTO.department().departmentCode());
            employee.setDepartment(department);
        }

        if (employeeFullDTO.status() != null) {
            EmploymentStatus status = new EmploymentStatus(employeeFullDTO.status().statusId());
            employee.setStatus(status);
        }

        if (employeeFullDTO.governmentId() != null) {
            GovernmentId governmentId = toGovernmentIdEntity(employeeFullDTO.governmentId());
            employee.setGovernmentId(governmentId);
        }

        if (employeeFullDTO.contacts() != null) {
            updateContacts(employeeFullDTO.contacts(), employee);
        }

        if (employeeFullDTO.benefits() != null) {
            updateBenefits(employeeFullDTO.benefits(), employee);
        }

        if (employeeFullDTO.leaveBalances() != null) {
            updateLeaveBalances(employeeFullDTO.leaveBalances(), employee);
        }
    }

    private void validateArguments(EmployeeDTO employeeFullDTO, Employee employee) {
        if (employeeFullDTO == null || employee == null) {
            throw new IllegalArgumentException("Employee DTO or employee cannot be null");
        }
    }

    /**
     * Updates the contacts of an employee based on the provided DTOs.
     * If a contact already exists, it updates the contact number.
     * If a contact does not exist, it creates a new contact and adds it to the employee.
     *
     * @param contactDTOs The list of contact DTOs to update.
     * @param employee    The employee whose contacts are being updated.
     */
    private void updateContacts(List<ContactDTO> contactDTOs, Employee employee) {
        // Get the existing contacts of the employee
        List<Contact> existingContacts = employee.getContacts();

        // Create a map to easily look up existing contacts by ID
        Map<Long, Contact> existingContactsMap = existingContacts.stream()
                .collect(Collectors.toMap(Contact::getContactId, contact -> contact));

        // Iterate over the contact DTOs
        for (ContactDTO contactDTO : contactDTOs) {
            Contact contact;

            // If the contact already exists, update its contact number
            if (contactDTO.contactId() != null && existingContactsMap.containsKey(contactDTO.contactId())) {
                contact = existingContactsMap.get(contactDTO.contactId());
                contact.setContactNo(contactDTO.contactNo());
            }
            // If the contact does not exist, create a new contact and add it to the employee
            else {
                contact = new Contact(contactDTO.contactNo());
                contact.setEmployee(employee);
                existingContacts.add(contact);
            }
        }

        // Update the employee's contacts with the updated list
        employee.setContacts(existingContacts);
    }

    /**
     * Updates the benefits of an employee based on the provided BenefitDTOs.
     * If a benefit already exists, it updates the amount.
     * If a benefit does not exist, it creates a new benefit and adds it to the employee.
     *
     * @param benefitDTOs the list of BenefitDTOs containing the updated benefits information
     * @param employee the Employee entity whose benefits need to be updated
     */
    private void updateBenefits(List<BenefitDTO> benefitDTOs, Employee employee) {
        // Get the existing benefits of the employee
        List<Benefits> existingBenefits = employee.getBenefits();

        // Create a map of existing benefits for efficient lookup
        Map<Long, Benefits> existingBenefitsMap = existingBenefits.stream()
                .collect(Collectors.toMap(Benefits::getBenefitId, benefit -> benefit));

        // Iterate over the provided BenefitDTOs to update the benefits
        for (BenefitDTO benefitDTO : benefitDTOs) {
            Benefits benefit;

            // Check if the BenefitDTO has a benefitId and is present in the existing benefits
            if (benefitDTO.benefitId() != null && existingBenefitsMap.containsKey(benefitDTO.employeeId())) {
                // Update the existing benefit with the new amount
                benefit = existingBenefitsMap.get(benefitDTO.employeeId());
                benefit.setAmount(benefitDTO.amount());
            } else {
                // Create a new benefit entity from the BenefitDTO and add it to the existing benefits
                benefit = benefitsMapper.toEntity(benefitDTO);
                benefit.setEmployee(employee);
                existingBenefits.add(benefit);
            }
        }

        // Update the employee's benefits with the updated list
        employee.setBenefits(existingBenefits);
    }

    /**
     * Updates the leave balances of an employee based on the provided DTOs.
     * If a DTO has an ID and corresponds to an existing leave balance, the balance is updated.
     * Otherwise, a new leave balance is created and added to the employee's list of leave balances.
     *
     * @param leaveBalanceDTOs The list of DTOs representing the leave balances to update or create.
     * @param employee The employee whose leave balances are being updated.
     */
    private void updateLeaveBalances(List<LeaveBalanceDTO> leaveBalanceDTOs, Employee employee) {
        // Get the existing leave balances of the employee
        List<LeaveBalance> existingLeaveBalances = employee.getLeaveBalances();

        // Create a map of existing leave balances for efficient lookup
        Map<Long, LeaveBalance> existingLeaveBalancesMap = existingLeaveBalances.stream()
                .collect(Collectors.toMap(LeaveBalance::getLeaveBalanceId, leaveBalance -> leaveBalance));

        // Iterate over the provided DTOs to update or create leave balances
        for (LeaveBalanceDTO leaveBalanceDTO : leaveBalanceDTOs) {
            LeaveBalance leaveBalance;

            // Check if the DTO has an ID and is present in the existing leave balances
            if (leaveBalanceDTO.id() != null && existingLeaveBalancesMap.containsKey(leaveBalanceDTO.id())) {
                // Update the existing leave balance with the new balance
                leaveBalance = existingLeaveBalancesMap.get(leaveBalanceDTO.id());
                leaveBalance.setBalance(leaveBalanceDTO.balance());
            } else {
                // Create a new leave balance entity from the DTO and add it to the existing leave balances
                leaveBalance = leaveBalanceMapper.toEntity(leaveBalanceDTO);
                leaveBalance.setEmployee(employee);
                existingLeaveBalances.add(leaveBalance);
            }
        }

        // Update the employee's leave balances with the updated list
        employee.setLeaveBalances(existingLeaveBalances);
    }

    private void validateEmployeeId(EmployeeDTO employeeFullDTO, Employee employee) {
        if (employeeFullDTO.employeeId() != null && !employee.getEmployeeId().equals(employeeFullDTO.employeeId())) {
            throw new IllegalArgumentException("Employee ID cannot be changed");
        }
    }
}
