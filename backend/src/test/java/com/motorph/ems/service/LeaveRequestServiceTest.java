package com.motorph.ems.service;

import com.motorph.ems.dto.LeaveRequestDTO;
import com.motorph.ems.dto.LeaveStatusDTO;
import com.motorph.ems.dto.mapper.LeaveRequestMapper;
import com.motorph.ems.model.*;
import com.motorph.ems.model.Employee.GovernmentId;
import com.motorph.ems.model.LeaveRequest.LeaveStatus;
import com.motorph.ems.repository.LeaveRequestRepository;
import com.motorph.ems.repository.LeaveStatusRepository;
import com.motorph.ems.service.impl.LeaveRequestServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository requestRepository;

    @Mock
    private LeaveRequestMapper requestMapper;

    @Mock
    private LeaveStatusRepository statusRepository;

    @InjectMocks
    private LeaveRequestServiceImpl leaveRequestService;

    private LeaveRequestDTO leaveRequestDTO;
    private LeaveRequest leaveRequest;
    private LeaveStatus leaveStatus;
    private LeaveStatusDTO leaveStatusDTO;

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

        Employee.Contact contact = Employee.Contact.builder()
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

        Employee employee1 = Employee.builder()
                .employeeId(2L)
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .contacts(Collections.singletonList(
                        Employee.Contact.builder().contactNo("1234567890").build()))
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

        leaveStatus = new LeaveStatus(1, "Pending");

        leaveStatusDTO = new LeaveStatusDTO(1, "Pending");

        leaveRequestDTO = LeaveRequestDTO.builder()
                .leaveRequestId(1L)
                .employeeId(employee1.getEmployeeId())
                .requestDate(LocalDate.now())
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .statusId(leaveStatus.getLeaveStatusId())
                .reason("Vacation")
                .build();

        leaveRequest = LeaveRequest.builder()
                .leaveRequestId(1L)
                .employee(employee1)
                .requestDate(LocalDate.now())
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .status(leaveStatus)
                .reason("Vacation")
                .build();
    }

    @Test
    @Transactional
    @DirtiesContext
    void addNewLeaveRequest() {
        when(requestRepository.existsByEmployeeIdAndDateRange(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(false);

        when(requestMapper.toEntity(any(LeaveRequestDTO.class))).thenReturn(leaveRequest);

        when(requestRepository.save(any(LeaveRequest.class))).thenReturn(leaveRequest);

        when(requestMapper.toDTO(any(LeaveRequest.class))).thenReturn(leaveRequestDTO);

        LeaveRequestDTO result = leaveRequestService.addNewLeaveRequest(leaveRequestDTO);

        assertNotNull(result);
        assertEquals(leaveRequestDTO.leaveRequestId(), result.leaveRequestId());
        verify(requestRepository, times(1)).save(any(LeaveRequest.class));
    }

    @Test
    @Transactional
    @DirtiesContext
    void addNewLeaveRequest_conflictingDates() {
        when(requestRepository.existsByEmployeeIdAndDateRange(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        assertThrows(IllegalStateException.class, () -> leaveRequestService.addNewLeaveRequest(leaveRequestDTO));
    }

    @Test
    @Transactional
    @DirtiesContext
    void getAllLeaveRequests() {
        when(requestRepository.findAll()).thenReturn(List.of(leaveRequest));

        when(requestMapper.toDTO(any(LeaveRequest.class))).thenReturn(leaveRequestDTO);

        List<LeaveRequestDTO> result = leaveRequestService.getAllLeaveRequests();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(leaveRequestDTO.leaveRequestId(), result.get(0).leaveRequestId());
    }

    @Test
    @Transactional
    @DirtiesContext
    void getLeaveRequestById() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(leaveRequest));

        when(requestMapper.toDTO(any(LeaveRequest.class))).thenReturn(leaveRequestDTO);

        Optional<LeaveRequestDTO> result = leaveRequestService.getLeaveRequestById(1L);

        assertTrue(result.isPresent());
        assertEquals(leaveRequestDTO.leaveRequestId(), result.get().leaveRequestId());
    }

    @Test
    @Transactional
    @DirtiesContext
    void updateLeaveRequest() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(leaveRequest));

        when(requestRepository.save(any(LeaveRequest.class))).thenReturn(leaveRequest);

        when(requestMapper.toDTO(any(LeaveRequest.class))).thenReturn(leaveRequestDTO);

        LeaveRequestDTO result = leaveRequestService.updateLeaveRequest(1L, leaveRequestDTO);

        assertNotNull(result);
        assertEquals(leaveRequestDTO.leaveRequestId(), result.leaveRequestId());
        verify(requestRepository, times(1)).save(any(LeaveRequest.class));
    }

    @Test
    @Transactional
    @DirtiesContext
    void updateLeaveRequest_notFound() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> leaveRequestService.updateLeaveRequest(10L, leaveRequestDTO));
    }

    @Test
    @Transactional
    @DirtiesContext
    void deleteLeaveRequest() {
        when(requestRepository.existsById(anyLong())).thenReturn(true);

        leaveRequestService.deleteLeaveRequest(1L);

        verify(requestRepository, times(1)).deleteById(1L);
    }

    @Test
    @Transactional
    @DirtiesContext
    void deleteLeaveRequest_notFound() {
        when(requestRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> leaveRequestService.deleteLeaveRequest(1L));
    }

    @Test
    @Transactional
    @DirtiesContext
    void getLeaveStatusById() {
        when(statusRepository.findById(anyInt())).thenReturn(Optional.of(leaveStatus));

        when(requestMapper.toDTO(any(LeaveStatus.class))).thenReturn(leaveStatusDTO);

        Optional<LeaveStatusDTO> result = leaveRequestService.getLeaveStatusById(1);

        assertTrue(result.isPresent());
        assertEquals(leaveStatus.getLeaveStatusId(), result.get().id());
    }

    @Test
    @Transactional
    @DirtiesContext
    void getAllLeaveStatus() {
        when(statusRepository.findAll()).thenReturn(List.of(leaveStatus));

        when(requestMapper.toDTO(any(LeaveStatus.class))).thenReturn(leaveStatusDTO);

        List<LeaveStatusDTO> result = leaveRequestService.getAllLeaveStatus();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(leaveStatus.getLeaveStatusId(), result.get(0).id());
    }
}
