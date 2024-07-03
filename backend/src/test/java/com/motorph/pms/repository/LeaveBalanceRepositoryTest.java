package com.motorph.ems.repository;

import com.motorph.ems.model.Employee;
import com.motorph.ems.model.LeaveBalance;
import com.motorph.ems.model.LeaveBalance.LeaveType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class LeaveBalanceRepositoryTest {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    private Employee employee1;
    private Employee employee2;
    private LeaveBalance balance1;
    private LeaveBalance balance2;
    private LeaveType leaveType1;
    private LeaveType leaveType2;

    @BeforeEach
    @Transactional
    @DirtiesContext
    void setUp() {
        leaveType1 = LeaveType.builder().type("Sick").build();
        leaveType2 = LeaveType.builder().type("Vacation").build();


        leaveTypeRepository.save(leaveType1);
        leaveTypeRepository.save(leaveType2);

        employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        employee2 = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        balance1 = LeaveBalance.builder()
                .employee(employee1)
                .leaveType(leaveType1)
                .balance(10)
                .build();

        balance2 = LeaveBalance.builder()
                .employee(employee2)
                .leaveType(leaveType2)
                .balance(5)
                .build();
    }

    @Test
    void LeaveBalanceRepository_Save_ReturnsSavedLeaveBalance() {
        LeaveBalance savedBalance = leaveBalanceRepository.save(balance1);

        assertThat(savedBalance.getEmployee()).isEqualTo(employee1);
        assertThat(savedBalance.getLeaveType()).isEqualTo(leaveType1);
        assertThat(savedBalance.getBalance()).isEqualTo(10);
    }

    @Test
    void LeaveBalanceRepository_SaveAll_ReturnsSavedLeaveBalances() {
        List<LeaveBalance> savedBalances = leaveBalanceRepository.saveAll(List.of(balance1, balance2));

        assertThat(savedBalances).hasSize(2);
        assertThat(savedBalances.get(0).getEmployee()).isEqualTo(employee1);
        assertThat(savedBalances.get(0).getLeaveType()).isEqualTo(leaveType1);
        assertThat(savedBalances.get(0).getBalance()).isEqualTo(10);
        assertThat(savedBalances.get(1).getEmployee()).isEqualTo(employee2);
        assertThat(savedBalances.get(1).getLeaveType()).isEqualTo(leaveType2);
        assertThat(savedBalances.get(1).getBalance()).isEqualTo(5);
    }

    @Test
    void LeaveBalanceRepository_FindById_ReturnsLeaveBalance() {
        LeaveBalance savedBalance = leaveBalanceRepository.save(balance1);

        LeaveBalance foundBalance = leaveBalanceRepository.findById(savedBalance.getLeaveBalanceId()).orElse(null);

        assertThat(foundBalance).isNotNull();
        assertThat(foundBalance.getEmployee()).isEqualTo(employee1);
        assertThat(foundBalance.getLeaveType()).isEqualTo(leaveType1);
        assertThat(foundBalance.getBalance()).isEqualTo(10);
    }

    @Test
    void LeaveBalanceRepository_FindByEmployeeId_ReturnsLeaveBalance() {
        LeaveBalance savedBalance = leaveBalanceRepository.save(balance2);

        LeaveBalance foundBalance = leaveBalanceRepository.findById(savedBalance.getLeaveBalanceId()).orElse(null);

        assertThat(foundBalance).isNotNull();
        assertThat(foundBalance.getEmployee()).isEqualTo(employee2);
        assertThat(foundBalance.getLeaveType()).isEqualTo(leaveType2);
        assertThat(foundBalance.getBalance()).isEqualTo(5);
    }

    @Test
    void LeaveBalanceRepository_FindAllByEmployeeId_ReturnsAllLeaveBalancesById() {
        LeaveBalance savedBalance1 = leaveBalanceRepository.save(balance1);
        balance2.setEmployee(employee1);
        LeaveBalance savedBalance2 = leaveBalanceRepository.save(balance2);

        List<LeaveBalance> foundBalances = leaveBalanceRepository.
                findAllByEmployee_EmployeeId(employee1.getEmployeeId());

        assertThat(foundBalances).hasSize(2);
        assertThat(foundBalances.get(0).getEmployee()).isEqualTo(employee1);
        assertThat(foundBalances.get(0).getLeaveType()).isEqualTo(leaveType1);
        assertThat(foundBalances.get(0).getBalance()).isEqualTo(10);
        assertThat(foundBalances.get(1).getEmployee()).isEqualTo(employee1);
        assertThat(foundBalances.get(1).getLeaveType()).isEqualTo(leaveType2);
        assertThat(foundBalances.get(1).getBalance()).isEqualTo(5);
    }


    @Test
    void LeaveBalanceRepository_FindByEmployeeIdAndLeaveType_ReturnsMatchingLeaveBalance() {
        leaveBalanceRepository.save(balance1);

        Optional<LeaveBalance> foundBalance = leaveBalanceRepository
                .getLeaveBalanceByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(employee1.getEmployeeId(), leaveType1.getLeaveTypeId());

        assertThat(foundBalance).isPresent();
        assertThat(foundBalance.get().getEmployee()).isEqualTo(employee1);
        assertThat(foundBalance.get().getLeaveType()).isEqualTo(leaveType1);
        assertThat(foundBalance.get().getBalance()).isEqualTo(10);
    }

    @Test
    void LeaveBalanceRepository_Update_ReturnsUpdatedLeaveBalance() {
        leaveBalanceRepository.save(balance1);

        balance1.setBalance(20);

        LeaveBalance updatedBalance = leaveBalanceRepository.save(balance1);

        assertThat(updatedBalance.getEmployee()).isEqualTo(employee1);
        assertThat(updatedBalance.getLeaveType()).isEqualTo(leaveType1);
        assertThat(updatedBalance.getBalance()).isEqualTo(20);
    }

    @Test
    void LeaveBalanceRepository_Delete_DeletesLeaveBalance() {
        leaveBalanceRepository.save(balance1);

        leaveBalanceRepository.deleteById(balance1.getLeaveBalanceId());

        Optional<LeaveBalance> foundBalance = leaveBalanceRepository
                .getLeaveBalanceByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(employee1.getEmployeeId(), leaveType1.getLeaveTypeId());

        assertThat(foundBalance).isEmpty();
    }
}