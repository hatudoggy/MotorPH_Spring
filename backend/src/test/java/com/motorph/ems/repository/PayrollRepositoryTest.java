package com.motorph.ems.repository;

import com.motorph.ems.model.Employee;
import com.motorph.ems.model.Payroll;
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
class PayrollRepositoryTest {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1, employee2;
    private Payroll payroll1, payroll2, payroll3;

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
                .lastName("Smith")
                .dob(LocalDate.now())
                .address("456 Elm St")
                .build();

        payroll1 = Payroll.builder()
                .employee(employee1)
                .periodStart(LocalDate.of(2023, 6, 1))
                .periodEnd(LocalDate.of(2023, 6, 15))
                .monthlyRate(5000.00)
                .dailyRate(200.00)
                .overtimePay(50.00)
                .grossIncome(2500.00)
                .netIncome(2000.00)
                .build();

        payroll2 = Payroll.builder()
                .employee(employee1)
                .periodStart(LocalDate.of(2023, 6, 16))
                .periodEnd(LocalDate.of(2023, 6, 30))
                .monthlyRate(5000.00)
                .dailyRate(200.00)
                .overtimePay(70.00)
                .grossIncome(2600.00)
                .netIncome(2100.00)
                .build();

        payroll3 = Payroll.builder()
                .employee(employee2)
                .periodStart(LocalDate.of(2023, 6, 1))
                .periodEnd(LocalDate.of(2023, 6, 15))
                .monthlyRate(4000.00)
                .dailyRate(160.00)
                .overtimePay(30.00)
                .grossIncome(2000.00)
                .netIncome(1700.00)
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        payrollRepository.save(payroll1);
        payrollRepository.save(payroll2);
        payrollRepository.save(payroll3);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PayrollRepository_savePayroll_returnsSavedPayroll() {
        Payroll savedPayroll1 = payrollRepository.save(payroll1);
        Payroll savedPayroll2 = payrollRepository.save(payroll2);

        assertNotNull(savedPayroll1);
        assertThat(savedPayroll1.getGrossIncome()).isEqualTo(2500.00);
        assertNotNull(savedPayroll2);
        assertThat(savedPayroll2.getGrossIncome()).isEqualTo(2600.00);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PayrollRepository_findByEmployee_EmployeeIdAndPeriodStart_Found() {
        Optional<Payroll> foundPayroll = payrollRepository.findByEmployee_EmployeeIdAndPeriodStart(employee1.getEmployeeId(), LocalDate.of(2023, 6, 1));
        assertThat(foundPayroll).isPresent();
        assertThat(foundPayroll.get().getGrossIncome()).isEqualTo(2500.00);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PayrollRepository_findAllByEmployee_EmployeeId_Found() {
        List<Payroll> payrollsForEmployee1 = payrollRepository.findAllByEmployee_EmployeeId(employee1.getEmployeeId());
        assertThat(payrollsForEmployee1).hasSize(2);
        assertThat(payrollsForEmployee1.get(0).getGrossIncome()).isEqualTo(2500.00);
        assertThat(payrollsForEmployee1.get(1).getGrossIncome()).isEqualTo(2600.00);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PayrollRepository_findAllByPeriodStartAndPeriodEnd_Found() {
        List<Payroll> payrollsForPeriod = payrollRepository.findAllByPeriodStartAndPeriodEnd(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 15));
        assertThat(payrollsForPeriod).hasSize(2);
        assertThat(payrollsForPeriod.get(0).getGrossIncome()).isEqualTo(2500.00);
        assertThat(payrollsForPeriod.get(1).getGrossIncome()).isEqualTo(2000.00);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PayrollRepository_deletePayroll() {
        payrollRepository.delete(payroll1);
        Optional<Payroll> deletedPayroll = payrollRepository.findByEmployee_EmployeeIdAndPeriodStart(employee1.getEmployeeId(), LocalDate.of(2023, 6, 1));
        assertThat(deletedPayroll).isNotPresent();
    }
}
