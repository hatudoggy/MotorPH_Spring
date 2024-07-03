package com.motorph.pms.repository;

import com.motorph.pms.model.*;
import com.motorph.pms.model.User.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentStatusRepository employmentStatusRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;
    
    private Employee employee1;

    private Employee employee2;

    private Department department;
    private Position position;
    private EmploymentStatus employmentStatus;
    
    @BeforeEach
    void setUp() {
        employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        employee2 = Employee.builder()
                .firstName("Anna")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        //Create departmentName and positionName
        department = Department.builder()
                .departmentCode("D01")
                .departmentName("Sales")
                .build();

        position = Position.builder()
                .positionCode("P01")
                .positionName("Manager")
                .build();

        // Save departmentName and positionName
        departmentRepository.save(department);
        positionRepository.save(position);

        //Create EmploymentStatus
        employmentStatus = EmploymentStatus.builder()
                .statusName("Regular")
                .build();

        // Save employmentStatus
        employmentStatusRepository.save(employmentStatus);

        //Add employees to departmentName and positionName
        employee1.setDepartment(department);
        employee1.setPosition(position);
        employee1.setStatus(employmentStatus);
        employee2.setDepartment(department);
        employee2.setPosition(position);
        employee2.setStatus(employmentStatus);
    } 
    

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_Save_ReturnsSavedEmployee() {
        //Act
        Employee savedEmployee = employeeRepository.save(employee1);

        //Assert
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getEmployeeId()).isGreaterThan(0L);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_getAllEmployees_ReturnsAllEmployees() {
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> employees = employeeRepository.findAll();

        assertThat(employees).hasSizeGreaterThan(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_getAllEmployees_ReturnsEmptyList() {
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).isEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_getAllEmployeesByEmploymentStatus_ReturnsMatchingEmployees() {
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        departmentRepository.save(department);
        positionRepository.save(position);
        employmentStatusRepository.save(employmentStatus);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        List<Employee> employees = employeeRepository.findAllByStatus_StatusName("Regular");
        assertThat(employees).hasSizeGreaterThan(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_getAllEmployeesByEmploymentStatus_ReturnsEmptyList() {
        List<Employee> employees = employeeRepository.findAllByStatus_StatusName("Regular");
        assertThat(employees).isEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_getById_ReturnsEmployee() {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        employeeRepository.save(employee);

        Employee found = employeeRepository.findById(employee.getEmployeeId()).get();

        assertThat(found.getFirstName()).isEqualTo("John");
        assertThat(found.getLastName()).isEqualTo("Doe");
        assertThat(found.getEmployeeId()).isGreaterThan(0);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_findByEmployeeNameFound() {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        employeeRepository.save(employee);

        Employee found = employeeRepository.findByFirstNameAndLastName("John", "Doe").get();
        assertThat(found.getFirstName()).isEqualTo("John");
        assertThat(found.getLastName()).isEqualTo("Doe");
        assertThat(found.getEmployeeId()).isGreaterThan(0);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_findByEmployeeNameNotFound() {
        Employee found = employeeRepository.findByFirstNameAndLastName("John", "Doe").orElse(null);
        assertThat(found).isNull();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_updateEmployee_ReturnsUpdatedEmployee() {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        Employee employeeSaved = employeeRepository.save(employee);

        employeeSaved.setFirstName("Jane");

        employeeSaved = employeeRepository.save(employeeSaved);

        assertThat(employeeSaved.getFirstName()).isEqualTo("Jane");
    }


    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_deleteEmployee_DeletesEmployee() {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        Employee employeeSaved = employeeRepository.save(employee);

        employeeRepository.delete(employeeSaved);

        Employee found = employeeRepository.findById(employeeSaved.getEmployeeId()).orElse(null);

        assertThat(found).isNull();
    }


    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_findAllBy_DepartmentName_Found() {
        employee1 = employeeRepository.save(employee1);
        employee2 = employeeRepository.save(employee2);

        departmentRepository.save(department);
        positionRepository.save(position);
        employmentStatusRepository.save(employmentStatus);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> employees = employeeRepository
                .findAllByDepartment_DepartmentName("Sales");

        assertThat(employees).hasSizeGreaterThan(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_findAllByPositionName_Found() {
        employee1 = employeeRepository.save(employee1);
        employee2 = employeeRepository.save(employee2);

        departmentRepository.save(department);
        positionRepository.save(position);
        employmentStatusRepository.save(employmentStatus);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> employees = employeeRepository
                .findAllByPosition_PositionName("Manager");

        assertThat(employees).hasSizeGreaterThan(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmployeeRepository_findAllByUser_RoleName_Found() {
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        Role role = Role.builder().roleName("IT").build();

        userRoleRepository.save(role);

        User user1 = User.builder()
                .username("john.doe")
                .employee(employee1)
                .role(role)
                .password("password")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .username("jane.doe")
                .employee(employee2)
                .role(role)
                .password("password")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

//        employee1.setUser(user1);
//        employee2.setUser(user2);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

//        List<Employee> employees = employeeRepository
//                .findAllByUser_Role_RoleName("IT");

//        assertThat(employees).hasSizeGreaterThan(1);
    }
}