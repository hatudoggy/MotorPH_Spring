package com.motorph.pms.repository;

import com.motorph.pms.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department1, department2;

    @BeforeEach
    void setUp() {
        department1 = Department.builder()
                .departmentCode("D001")
                .departmentName("Human Resources")
                .build();

        department2 = Department.builder()
                .departmentCode("D002")
                .departmentName("Finance")
                .build();

        departmentRepository.save(department1);
        departmentRepository.save(department2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void DepartmentRepository_findByDepartmentName_Found() {
        Optional<Department> foundDepartment = departmentRepository.findByDepartmentName("Human Resources");

        assertTrue(foundDepartment.isPresent());
        assertThat(foundDepartment.get()).isEqualTo(department1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void DepartmentRepository_findByDepartmentName_NotFound() {
        Optional<Department> foundDepartment = departmentRepository.findByDepartmentName("Nonexistent Department");

        assertThat(foundDepartment).isNotPresent();
    }

    @Test
    @Transactional
    @DirtiesContext
    void DepartmentRepository_updateDepartment_ReturnsUpdatedDepartment() {
        // Retrieve the departmentName
        Department existingDepartment = departmentRepository.findById(department1.getDepartmentCode()).orElse(null);
        assertNotNull(existingDepartment);

        // Update the departmentName name
        String newDepartmentName = "HR";
        existingDepartment.setDepartmentName(newDepartmentName);
        Department updatedDepartment = departmentRepository.save(existingDepartment);

        // Verify the update
        assertNotNull(updatedDepartment);
        assertThat(updatedDepartment.getDepartmentName()).isEqualTo(newDepartmentName);
    }

    @Test
    @Transactional
    @DirtiesContext
    void DepartmentRepository_deleteDepartment() {
        // Save a new departmentName to delete later
        Department departmentToDelete = Department.builder()
                .departmentCode("D003")
                .departmentName("IT")
                .build();
        departmentRepository.save(departmentToDelete);

        // Verify that it's saved
        assertNotNull(departmentRepository.findById(departmentToDelete.getDepartmentCode()));

        // Delete the departmentName
        departmentRepository.delete(departmentToDelete);

        // Verify that it's deleted
        Optional<Department> deletedDepartment = departmentRepository.findById(departmentToDelete.getDepartmentCode());
        assertThat(deletedDepartment).isNotPresent();
    }
}
