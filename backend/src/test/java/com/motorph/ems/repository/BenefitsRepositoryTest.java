package com.motorph.ems.repository;

import com.motorph.ems.model.*;
import com.motorph.ems.model.Benefits.BenefitType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BenefitsRepositoryTest {

    @Autowired
    private BenefitsRepository benefitsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentStatusRepository employmentStatusRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private BenefitTypeRepository benefitTypeRepository;

    private Employee employee1, employee2;
    private Benefits benefit1, benefit2;
    private BenefitType benefitType1, benefitType2;
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

        //Create department and position
        department = Department.builder()
                .departmentCode("D01")
                .departmentName("Sales")
                .build();

        position = Position.builder()
                .positionCode("P01")
                .positionName("Sales Representative")
                .build();

        // Save department and position
        departmentRepository.save(department);
        positionRepository.save(position);

        //Create EmploymentStatus
        employmentStatus = EmploymentStatus.builder()
                .statusName("Permanent")
                .build();

        // Save employmentStatus
        employmentStatusRepository.save(employmentStatus);

        //Add employees to department and position
        employee1.setDepartment(department);
        employee1.setPosition(position);
        employee1.setStatus(employmentStatus);
        employee2.setDepartment(department);
        employee2.setPosition(position);
        employee2.setStatus(employmentStatus);

        benefitType1 = BenefitType.builder().benefit("Health Insurance").build();
        benefitType2 = BenefitType.builder().benefit("Pension").build();

        benefitTypeRepository.save(benefitType1);
        benefitTypeRepository.save(benefitType2);

        benefit1 = Benefits.builder()
                .employee(employee1)
                .amount(5000.0)
                .benefitType(benefitType1)
                .build();

        benefit2 = Benefits.builder()
                .employee(employee1)
                .amount(3000.0)
                .benefitType(benefitType2)
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsRepository_saveBenefit_returnsSavedBenefit() {

        benefitsRepository.save(benefit1);
        benefitsRepository.save(benefit2);

        assertNotNull(benefit1);
        assertThat(benefit1.getEmployee()).isEqualTo(employee1);
        assertNotNull(benefit2);
        assertThat(benefit2.getEmployee()).isEqualTo(employee1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsRepository_findBenefitsByEmployeeId_Found() {

        benefitsRepository.save(benefit1);
        benefitsRepository.save(benefit2);

        List<Benefits> benefits = benefitsRepository.findByEmployee_EmployeeId(employee1.getEmployeeId());

        assertThat(benefits).hasSizeGreaterThan(1);
        assertThat(benefits).containsExactlyInAnyOrder(benefit1, benefit2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsRepository_findBenefitsByEmployeeName_Found() {

        benefitsRepository.save(benefit1);
        benefitsRepository.save(benefit2);

        List<Benefits> benefits = benefitsRepository.findAllByEmployee_FirstNameAndEmployee_LastName("John", "Doe");

        assertThat(benefits).hasSizeGreaterThan(1);
        assertThat(benefits).containsExactlyInAnyOrder(benefit1, benefit2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsRepository_findBenefitsByPositionCode_Found() {
        benefitsRepository.save(benefit1);
        benefitsRepository.save(benefit2);

        List<Benefits> benefits = benefitsRepository.
                findAllByEmployee_Position_PositionCode("P01");

        assertThat(benefits).hasSizeGreaterThan(1);
        assertThat(benefits).containsExactlyInAnyOrder(benefit1, benefit2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsRepository_getAllBenefits_Found() {

        benefitsRepository.save(benefit1);
        benefitsRepository.save(benefit2);

        List<Benefits> benefits = benefitsRepository.findAll();
        assertThat(benefits).hasSizeGreaterThan(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsRepository_getAllBenefits_ReturnsEmptyList() {
        List<Benefits> benefits = benefitsRepository.findAll();
        assertThat(benefits).isEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsRepository_updateBenefit_ReturnsUpdatedBenefit() {

        benefitsRepository.save(benefit1);

        Double newAmount = 6000.0;
        Double previousAmount = benefit1.getAmount();
        benefit1.setAmount(newAmount);

        Benefits updatedBenefit = benefitsRepository.save(benefit1);

        assertThat(updatedBenefit.getAmount()).isEqualTo(newAmount);
        assertThat(updatedBenefit.getAmount()).isNotEqualTo(previousAmount);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsRepository_deleteBenefit() {
        benefitsRepository.save(benefit1);
        benefitsRepository.delete(benefit1);
        List<Benefits> benefits = benefitsRepository.findAll();
        assertThat(benefits).isEmpty();
    }
}
