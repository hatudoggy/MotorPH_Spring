package com.motorph.ems.service;

import com.motorph.ems.dto.EmployeeDTO;
import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.dto.mapper.PayrollMapper;
import com.motorph.ems.model.*;
import com.motorph.ems.model.Employee.GovernmentId;
import com.motorph.ems.repository.PayrollRepository;
import com.motorph.ems.service.impl.PayrollServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayrollServiceTest {

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private PayrollMapper payrollMapper;

    @InjectMocks
    private PayrollServiceImpl payrollService;

    private PayrollDTO payrollDTO;
    private Payroll payroll;

    @BeforeEach
    void setUp() {
        Employee built_emp = Employee.builder()
                .employeeId(2L)
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        EmployeeDTO employeeDTO = EmployeeDTO.builder()
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
                .contact_id(2L)
                .employee(built_emp)
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

        payrollDTO = PayrollDTO.builder()
                .payrollId(1L)
                .employee(employeeDTO)
                .periodStart(LocalDate.now().withDayOfMonth(1))
                .periodEnd(LocalDate.now().withDayOfMonth(30))
                .monthlyRate(3000.00)
                .hourlyRate(100.00)
                .overtimePay(50.00)
                .grossIncome(3100.00)
                .netPay(2900.00)
                .build();

        payroll = Payroll.builder()
                .payrollId(1L)
                .employee(employee1)
                .periodStart(LocalDate.now().withDayOfMonth(1))
                .periodEnd(LocalDate.now().withDayOfMonth(30))
                .monthlyRate(3000.00)
                .hourlyRate(100.00)
                .overtimePay(50.00)
                .grossIncome(3100.00)
                .netPay(2900.00)
                .build();
    }

    @Test
    void addNewPayroll() {
        when(payrollRepository.existsByEmployee_EmployeeIdAndPeriodStart(anyLong(), any(LocalDate.class)))
                .thenReturn(false);

        when(payrollMapper.toEntity(any(PayrollDTO.class))).thenReturn(payroll);

        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);

        when(payrollMapper.toDTO(any(Payroll.class))).thenReturn(payrollDTO);

        PayrollDTO result = payrollService.addNewPayroll(payrollDTO);

        assertNotNull(result);
        assertEquals(payrollDTO.payrollId(), result.payrollId());
        verify(payrollRepository, times(1)).save(any(Payroll.class));
    }

    @Test
    void addNewPayroll_conflictingDates() {
        when(payrollRepository.existsByEmployee_EmployeeIdAndPeriodStart(anyLong(), any(LocalDate.class)))
                .thenReturn(true);

        assertThrows(IllegalStateException.class, () -> payrollService.addNewPayroll(payrollDTO));
    }

    @Test
    void getAllPayrolls() {
        when(payrollRepository.findAll()).thenReturn(List.of(payroll));

        when(payrollMapper.toDTO(any(Payroll.class))).thenReturn(payrollDTO);

        List<PayrollDTO> result = payrollService.getAllPayrolls();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(payrollDTO.payrollId(), result.iterator().next().payrollId());
    }

    @Test
    void getPayrollById() {
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));

        when(payrollMapper.toDTO(any(Payroll.class))).thenReturn(payrollDTO);

        Optional<PayrollDTO> result = payrollService.getPayrollById(1L);

        assertTrue(result.isPresent());
        assertEquals(payrollDTO.payrollId(), result.get().payrollId());
    }

    @Test
    void getPayrollByEmployeeIdAndPeriodStart() {
        when(payrollRepository.findByEmployee_EmployeeIdAndPeriodStart(anyLong(), any(LocalDate.class)))
                .thenReturn(Optional.of(payroll));

        when(payrollMapper.toDTO(any(Payroll.class))).thenReturn(payrollDTO);

        Optional<PayrollDTO> result = payrollService.getPayrollByEmployeeIdAndPeriodStart(1L, LocalDate.now().withDayOfMonth(1));

        assertTrue(result.isPresent());
        assertEquals(payrollDTO.payrollId(), result.get().payrollId());
    }

    @Test
    void updatePayroll() {
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));
        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);

        when(payrollMapper.toDTO(any(Payroll.class))).thenReturn(payrollDTO);

        PayrollDTO result = payrollService.updatePayroll(1L, payrollDTO);

        assertNotNull(result);
        assertEquals(payrollDTO.payrollId(), result.payrollId());
        verify(payrollRepository, times(1)).save(any(Payroll.class));
    }

    @Test
    void updatePayroll_notFound() {
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> payrollService.updatePayroll(1L, payrollDTO));
    }

    @Test
    void deletePayroll() {
        when(payrollRepository.existsById(anyLong())).thenReturn(true);

        payrollService.deletePayroll(1L);

        verify(payrollRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePayroll_notFound() {
        when(payrollRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> payrollService.deletePayroll(1L));
    }

    @Test
    void getPayrollsByEmployeeId() {
        when(payrollRepository.findAllByEmployee_EmployeeId(anyLong())).thenReturn(List.of(payroll));

        when(payrollMapper.toDTO(any(Payroll.class))).thenReturn(payrollDTO);

        List<PayrollDTO> result = payrollService.getPayrollsByEmployeeId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(payrollDTO.payrollId(), result.iterator().next().payrollId());
    }

    @Test
    void getPayrollsForPeriod() {
        when(payrollRepository.findAllByPeriodStartAndPeriodEnd(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(payroll));

        when(payrollMapper.toDTO(any(Payroll.class))).thenReturn(payrollDTO);

        List<PayrollDTO> result = payrollService.getPayrollsForPeriod(LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(30));

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(payrollDTO.payrollId(), result.iterator().next().payrollId());
    }
}
