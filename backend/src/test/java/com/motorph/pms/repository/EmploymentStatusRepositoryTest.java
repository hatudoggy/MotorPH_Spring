package com.motorph.ems.repository;

import com.motorph.ems.model.EmploymentStatus;
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
class EmploymentStatusRepositoryTest {

    @Autowired
    private EmploymentStatusRepository employmentStatusRepository;

    private EmploymentStatus status1, status2;

    @BeforeEach
    void setUp() {
        status1 = EmploymentStatus.builder()
                .statusName("Active")
                .build();

        status2 = EmploymentStatus.builder()
                .statusName("Inactive")
                .build();

        employmentStatusRepository.save(status1);
        employmentStatusRepository.save(status2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmploymentStatusRepository_findByStatusName_Found() {
        Optional<EmploymentStatus> foundStatus = employmentStatusRepository.findByStatusName("Active");

        assertTrue(foundStatus.isPresent());
        assertThat(foundStatus.get()).isEqualTo(status1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmploymentStatusRepository_findByStatusName_NotFound() {
        Optional<EmploymentStatus> foundStatus = employmentStatusRepository.findByStatusName("Nonexistent Status");

        assertThat(foundStatus).isNotPresent();
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmploymentStatusRepository_updateStatus_ReturnsUpdatedStatus() {
        // Retrieve the statusName
        EmploymentStatus existingStatus = employmentStatusRepository.findById(status1.getStatusId()).orElse(null);
        assertNotNull(existingStatus);

        // Update the statusName name
        String newStatusName = "Retired";
        existingStatus.setStatusName(newStatusName);
        EmploymentStatus updatedStatus = employmentStatusRepository.save(existingStatus);

        // Verify the update
        assertNotNull(updatedStatus);
        assertThat(updatedStatus.getStatusName()).isEqualTo(newStatusName);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmploymentStatusRepository_deleteStatus() {
        // Save a new statusName to delete later
        EmploymentStatus statusToDelete = EmploymentStatus.builder()
                .statusName("Probation")
                .build();
        employmentStatusRepository.save(statusToDelete);

        // Verify that it's saved
        assertNotNull(employmentStatusRepository.findById(statusToDelete.getStatusId()));

        // Delete the statusName
        employmentStatusRepository.delete(statusToDelete);

        // Verify that it's deleted
        Optional<EmploymentStatus> deletedStatus = employmentStatusRepository.findById(statusToDelete.getStatusId());
        assertThat(deletedStatus).isNotPresent();
    }
}
