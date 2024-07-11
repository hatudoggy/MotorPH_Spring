package com.motorph.pms.repository;

import com.motorph.pms.model.*;
import com.motorph.pms.model.EmploymentStatus;
import com.motorph.pms.model.LeaveRequest.LeaveStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class LeaveRequestRepositoryTest {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentStatusRepository employmentStatusRepository;

    @Autowired
    private LeaveStatusRepository leaveStatusRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    private Employee employee1, employee2, supervisor;
    private LeaveRequest leaveRequest1, leaveRequest2, leaveRequest3;
    private LeaveStatus statusApproved, statusPending;
    private Department department;
    private Position position;
    private EmploymentStatus employmentStatus;

    @BeforeEach
    void setUp() {
        employee1 = Employee.builder()
                .firstName("Jim")
                .lastName("Halpert")
                .dob(LocalDate.now())
                .address("Scranton")
                .build();

        employee2 = Employee.builder()
                .firstName("Pam")
                .lastName("Beesly")
                .dob(LocalDate.now())
                .address("Scranton")
                .build();

        supervisor = Employee.builder()
                .firstName("Michael")
                .lastName("Scott")
                .dob(LocalDate.now())
                .address("Scranton")
                .build();
        
        employeeRepository.save(supervisor);

        //Create departmentName and positionName
        department = Department.builder()
                .departmentCode("D01")
                .departmentName("Sales")
                .build();

        position = Position.builder()
                .positionCode("P01")
                .positionName("Sales Representative")
                .build();

        // Save departmentName and positionName
        departmentRepository.save(department);
        positionRepository.save(position);

        //Create EmploymentStatus
        employmentStatus = EmploymentStatus.builder()
                .statusName("Permanent")
                .build();

        // Save employmentStatus
        employmentStatusRepository.save(employmentStatus);

        //Add employees to departmentName and positionName
        employee1.setDepartment(department);
        employee1.setPosition(position);
        employee1.setStatus(employmentStatus);
        employee1.setSupervisor(supervisor);
        employee2.setDepartment(department);
        employee2.setPosition(position);
        employee2.setStatus(employmentStatus);
        employee2.setSupervisor(supervisor);

        // Save employees
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        
        
        // Create LeaveStatus
        statusApproved = new LeaveStatus("Approved");
        statusPending = new LeaveStatus("Pending");

        leaveStatusRepository.save(statusApproved);
        leaveStatusRepository.save(statusPending);

        // Create LeaveRequests
        leaveRequest1 = LeaveRequest.builder()
                .employee(employee1)
                .requestDate(LocalDate.of(2023, 6, 1))
                .startDate(LocalDate.of(2023, 6, 10))
                .endDate(LocalDate.of(2023, 6, 15))
                .status(statusApproved)
                .reason("Vacation")
                .build();

        leaveRequest2 = LeaveRequest.builder()
                .employee(employee1)
                .requestDate(LocalDate.of(2023, 6, 20))
                .startDate(LocalDate.of(2023, 6, 25))
                .endDate(LocalDate.of(2023, 6, 30))
                .status(statusPending)
                .reason("Medical")
                .build();

        leaveRequest3 = LeaveRequest.builder()
                .employee(employee2)
                .requestDate(LocalDate.of(2023, 6, 5))
                .startDate(LocalDate.of(2023, 6, 10))
                .endDate(LocalDate.of(2023, 6, 15))
                .status(statusApproved)
                .reason("Personal")
                .build();

        // Save LeaveRequests
        leaveRequestRepository.save(leaveRequest1);
        leaveRequestRepository.save(leaveRequest2);
        leaveRequestRepository.save(leaveRequest3);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveRequestRepository_SaveNewLeaveRequest() {
        // Create a new LeaveRequest
        LeaveRequest newRequest = LeaveRequest.builder()
                .employee(employee1)
                .requestDate(LocalDate.of(2023, 7, 1))
                .startDate(LocalDate.of(2023, 7, 10))
                .endDate(LocalDate.of(2023, 7, 15))
                .status(statusPending)
                .reason("Family emergency")
                .build();

        LeaveRequest savedRequest = leaveRequestRepository.save(newRequest);

        // Verify the new request is saved
        assertNotNull(savedRequest.getLeaveRequestId());

        // Retrieve the saved request and verify its attributes
        Optional<LeaveRequest> retrievedRequest = leaveRequestRepository.findById(savedRequest.getLeaveRequestId());
        assertThat(retrievedRequest).isPresent();
        assertThat(retrievedRequest.get().getEmployee().getEmployeeId()).isEqualTo(employee1.getEmployeeId());
        assertThat(retrievedRequest.get().getRequestDate()).isEqualTo(LocalDate.of(2023, 7, 1));
        assertThat(retrievedRequest.get().getStartDate()).isEqualTo(LocalDate.of(2023, 7, 10));
        assertThat(retrievedRequest.get().getEndDate()).isEqualTo(LocalDate.of(2023, 7, 15));
        assertThat(retrievedRequest.get().getStatus().getStatusName()).isEqualTo("Pending");
        assertThat(retrievedRequest.get().getReason()).isEqualTo("Family emergency");
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveRequestRepository_findAll() {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        assertThat(leaveRequests).hasSize(3);
        assertThat(leaveRequests.get(0).getReason()).isEqualTo("Vacation");
        assertThat(leaveRequests.get(1).getReason()).isEqualTo("Medical");
        assertThat(leaveRequests.get(2).getReason()).isEqualTo("Personal");
    }


    @Test
    @Transactional
    @DirtiesContext
    void LeaveRequestRepository_findAllByEmployee_EmployeeId_Found() {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAllByEmployee_EmployeeId(employee1.getEmployeeId());
        assertThat(leaveRequests).hasSize(2);
        assertThat(leaveRequests.get(0).getReason()).isEqualTo("Vacation");
        assertThat(leaveRequests.get(1).getReason()).isEqualTo("Medical");
    }


    @Test
    @Transactional
    @DirtiesContext
    void LeaveRequestRepositoryUpdateLeaveRequest() {
        // Modify an existing LeaveRequest
        leaveRequest2.setStartDate(LocalDate.of(2023, 6, 26));
        leaveRequest2.setEndDate(LocalDate.of(2023, 7, 1));
        leaveRequest2.setStatus(statusApproved);
        leaveRequest2.setReason("Updated reason");

        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest2);

        // Verify the existing request is updated
        Optional<LeaveRequest> retrievedRequest = leaveRequestRepository.findById(updatedRequest.getLeaveRequestId());
        assertThat(retrievedRequest).isPresent();
        assertThat(retrievedRequest.get().getStartDate()).isEqualTo(LocalDate.of(2023, 6, 26));
        assertThat(retrievedRequest.get().getEndDate()).isEqualTo(LocalDate.of(2023, 7, 1));
        assertThat(retrievedRequest.get().getStatus().getStatusName()).isEqualTo("Approved");
        assertThat(retrievedRequest.get().getReason()).isEqualTo("Updated reason");
    }
}
