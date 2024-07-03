package com.motorph.ems.service;

import com.motorph.ems.dto.EmployeeDTO;
import com.motorph.ems.dto.mapper.EmployeeMapper;
import com.motorph.ems.model.*;
import com.motorph.ems.model.Employee.Contact;
import com.motorph.ems.model.Employee.GovernmentId;
import com.motorph.ems.repository.EmployeeRepository;
import com.motorph.ems.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee1;
    private EmployeeDTO employeeFullDTO1;
    private final LocalDate hireDate = LocalDate.of(2022,1,1);
    private final LocalDate hireDate2 = LocalDate.now();

    @BeforeEach
    void setUp() {
        Employee employee = Employee.builder()
                .employeeId(2L)
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        Department department = Department.builder()
                .departmentCode("IT")
                .departmentName("Information Technology")
                .build();
        Position position = Position.builder()
                .positionCode("P01")
                .department(department)
                .positionName("Manager")
                .build();

        Contact contact = Contact.builder()
                .contactId(2L)
                .employee(employee)
                .contactNo("1234567890").build();

        Employee supervisor = Employee.builder()
                .employeeId(1L)
                .firstName("Alex")
                .lastName("Smith")
                .address("24 Main St")
                .contacts(List.of(contact))
                .position(position)
                .build();

        employee1 = Employee.builder()
                .employeeId(2L)
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .contacts(Collections.singletonList(
                        Contact.builder().contactNo("1234567890").build()))
                .hireDate(LocalDate.now())
                .basicSalary(90000.00)
                .semiMonthlyRate(45000.00)
                .hourlyRate(400.00)
                .position(position)
                .department(department)
                .governmentId(GovernmentId.builder()
                        .id(1L)
                        .build())
                .status(EmploymentStatus.builder()
                        .statusId(1)
                        .statusName("Active")
                        .build())
                .supervisor(supervisor)
                .build();

        employeeFullDTO1 = EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dob(employee.getDob())
                .address(employee.getAddress())
                .hireDate(employee.getHireDate())
                .build();


        // List employment for employee1 after employment object is created
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_addNewEmployee_ReturnsEmployee() {

        when(employeeRepository.existsByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(false);

        when(employeeMapper.toEntity(any(EmployeeDTO.class))).thenReturn(employee1);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

        when(employeeMapper.toFullDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        EmployeeDTO savedEmployee = employeeService.addNewEmployee(employeeFullDTO1);


        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployeeById_ReturnsEmployeeDTO() {
        Optional<Employee> employee = Optional.of(employee1);

        when(employeeRepository.findById(any(Long.class))).thenReturn(employee);

        when(employeeMapper.toFullDTO(any(Employee.class))).thenReturn(employeeFullDTO1);


        Optional<EmployeeDTO> foundEmployee = employeeService.getEmployeeById(1L, true);

        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().firstName()).isEqualTo("John");
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployees_ReturnsListOfEmployeeDTO() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee1));

        when(employeeMapper.toLimitedDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        List<EmployeeDTO> employees = employeeService.getEmployees();

        assertThat(employees).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_deleteEmployee_DeletesEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployeeByName_ReturnsEmployee() {
        when(employeeRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.of(employee1));

        when(employeeMapper.toFullDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        Optional<EmployeeDTO> foundEmployee = employeeService.getEmployeeByName("John", "Doe");

        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().firstName()).isEqualTo("John");
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployeesByDepartment_ReturnsListOfEmployeeDTO() {
        when(employeeRepository.findAllByDepartment_DepartmentName("IT"))
                .thenReturn(Collections.singletonList(employee1));

        when(employeeMapper.toLimitedDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment("IT");

        assertThat(employees).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployeesByPosition_ReturnsListOfEmployeeDTO() {
        when(employeeRepository.findAllByPosition_PositionName("Manager"))
                .thenReturn(Collections.singletonList(employee1));

        when(employeeMapper.toLimitedDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        List<EmployeeDTO> employees = employeeService.getEmployeesByPosition("Manager");

        assertThat(employees).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployeesByStatus_ReturnsListOfEmployeeDTO() {
        when(employeeRepository.findAllByStatus_StatusName("Regular"))
                .thenReturn(Collections.singletonList(employee1));

        when(employeeMapper.toLimitedDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        List<EmployeeDTO> employees = employeeService.getEmployeesByStatus("Regular");

        assertThat(employees).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployeesBySupervisorId_ReturnsListOfEmployeeDTO() {
        when(employeeRepository.findAllBySupervisor_EmployeeId(any(Long.class)))
                .thenReturn(Collections.singletonList(employee1));

        when(employeeMapper.toLimitedDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        List<EmployeeDTO> employees = employeeService.getEmployeesBySupervisorId(1L);

        assertThat(employees).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployeesBySupervisorName_ReturnsListOfEmployeeDTO() {
        when(employeeRepository.findAllBySupervisor_FirstName_AndSupervisor_LastName("John", "Doe"))
                .thenReturn(Collections.singletonList(employee1));

        when(employeeMapper.toLimitedDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        List<EmployeeDTO> employees = employeeService.getEmployeesBySupervisorName("John", "Doe");

        assertThat(employees).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_getEmployeesByHireDate_Between_ReturnsListOfEmployeeDTO() {
        when(employeeRepository.findAllByHireDateBetween(hireDate, hireDate2))
                .thenReturn(Collections.singletonList(employee1));

        when(employeeMapper.toLimitedDTO(any(Employee.class))).thenReturn(employeeFullDTO1);

        List<EmployeeDTO> employees = employeeService.getEmployeesByHiredBetween(hireDate, hireDate2);

        assertThat(employees).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeService_updateEmployee_UpdatesAndReturnsEmployee() {
        Employee updatedEmployee = employee1;

        when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.of(employee1));

        updatedEmployee.setFirstName("Alex");

        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        EmployeeDTO updatedDTO = EmployeeDTO.builder()
                .employeeId(2L)
                .firstName("Alex")
                .build();

        when(employeeMapper.toFullDTO(any(Employee.class))).thenReturn(updatedDTO);


        EmployeeDTO updatedEmployeeFullDTO = employeeService.updateEmployee(updatedEmployee.getEmployeeId(), employeeFullDTO1);

        assertThat(updatedEmployeeFullDTO).isNotNull();
        assertThat(updatedEmployeeFullDTO.employeeId()).isEqualTo(employee1.getEmployeeId());
        assertThat(updatedEmployeeFullDTO.firstName()).isEqualTo("Alex");
    }
}
