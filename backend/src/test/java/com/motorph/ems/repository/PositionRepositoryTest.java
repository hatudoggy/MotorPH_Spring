package com.motorph.ems.repository;

import com.motorph.ems.model.Department;
import com.motorph.ems.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PositionRepositoryTest {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department1, department2;
    private Position position1, position2;

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

        position1 = Position.builder()
                .positionCode("P001")
                .positionName("Manager")
                .department(department1)
                .build();

        position2 = Position.builder()
                .positionCode("P002")
                .positionName("Analyst")
                .department(department1)
                .build();

        positionRepository.save(position1);
        positionRepository.save(position2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionRepository_findByPositionName_Found() {
        Optional<Position> foundPosition = positionRepository.findByPositionName("Manager");

        assertTrue(foundPosition.isPresent());
        assertThat(foundPosition.get()).isEqualTo(position1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionRepository_findByPositionName_NotFound() {
        Optional<Position> foundPosition = positionRepository.findByPositionName("Nonexistent Position");

        assertThat(foundPosition).isNotPresent();
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionRepository_findAllByDepartment_DepartmentCode_Found() {
        List<Position> positions = positionRepository.findAllByDepartment_DepartmentCode("D001");

        assertThat(positions).hasSize(2);
        assertThat(positions).contains(position1, position2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionRepository_findAllByDepartment_DepartmentCode_NotFound() {
        List<Position> positions = positionRepository.findAllByDepartment_DepartmentCode("D003");

        assertThat(positions).isEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionRepository_updatePosition_ReturnsUpdatedPosition() {
        // Retrieve the position
        Position existingPosition = positionRepository.findById(position1.getPositionCode()).orElse(null);
        assertNotNull(existingPosition);

        // Update the position name
        String newPositionName = "Senior Manager";
        existingPosition.setPositionName(newPositionName);
        Position updatedPosition = positionRepository.save(existingPosition);

        // Verify the update
        assertNotNull(updatedPosition);
        assertThat(updatedPosition.getPositionName()).isEqualTo(newPositionName);
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionRepository_deletePosition() {
        // Save a new position to delete later
        Position positionToDelete = Position.builder()
                .positionCode("P003")
                .positionName("Consultant")
                .department(department2)
                .build();
        positionRepository.save(positionToDelete);

        // Verify that it's saved
        assertNotNull(positionRepository.findById(positionToDelete.getPositionCode()));

        // Delete the position
        positionRepository.delete(positionToDelete);

        // Verify that it's deleted
        Optional<Position> deletedPosition = positionRepository.findById(positionToDelete.getPositionCode());
        assertThat(deletedPosition).isNotPresent();
    }
}
