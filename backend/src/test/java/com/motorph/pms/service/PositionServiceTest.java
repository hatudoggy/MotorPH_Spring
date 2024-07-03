package com.motorph.pms.service;

import com.motorph.pms.dto.PositionDTO;
import com.motorph.pms.dto.mapper.PositionMapper;
import com.motorph.pms.model.Department;
import com.motorph.pms.model.Position;
import com.motorph.pms.repository.PositionRepository;
import com.motorph.pms.service.impl.PositionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PositionMapper positionMapper;

    @InjectMocks
    private PositionServiceImpl positionService;

    private Department department;
    private Position position1;
    private PositionDTO positionDTO1;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .departmentCode("D001")
                .departmentName("HR")
                .build();

        position1 = Position.builder()
                .positionCode("P001")
                .positionName("Manager")
                .department(department)
                .build();

        positionDTO1 = PositionDTO.builder()
                .positionCode("P001")
                .positionName("Manager")
                .departmentCode(department.getDepartmentCode())
                .build();
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionService_addPosition_ReturnsPositionDTO() {
        when(positionRepository.existsById(any(String.class))).thenReturn(false);

        when(positionRepository.findByPositionName(any(String.class))).thenReturn(Optional.empty());

        when(positionMapper.toEntity(any(PositionDTO.class))).thenReturn(position1);

        when(positionRepository.save(any(Position.class))).thenReturn(position1);

        when(positionMapper.toDTO(any(Position.class))).thenReturn(positionDTO1);

        PositionDTO savedPosition = positionService.addPosition(positionDTO1);

        assertThat(savedPosition).isNotNull();
        assertThat(savedPosition.positionCode()).isEqualTo("P001");
        assertThat(savedPosition.positionName()).isEqualTo("Manager");
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionService_getPositionsByDepartment_ReturnsListOfPositionByPositionCodeDTO() {
        when(positionRepository.findAllByDepartment_DepartmentCode("D001")).thenReturn(List.of(position1));

        when(positionMapper.toDTO(any(Position.class))).thenReturn(positionDTO1);

        List<PositionDTO> positions = positionService.getPositionsByDepartment("D001");

        assertThat(positions).isNotEmpty();
        assertThat(positions.get(0).positionCode()).isEqualTo("P001");
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionService_getPositions_ReturnsListOfPositionByPositionCodeDTO() {
        when(positionRepository.findAll()).thenReturn(List.of(position1));

        when(positionMapper.toDTO(any(Position.class))).thenReturn(positionDTO1);

        List<PositionDTO> positions = positionService.getPositions();

        assertThat(positions).isNotEmpty();
        assertThat(positions.get(0).positionCode()).isEqualTo("P001");
    }

    @Test
    @Transactional
    @DirtiesContext
    void PositionService_getPosition_ReturnsPositionByPositionCodeDTO() {
        when(positionRepository.findById("P001")).thenReturn(Optional.of(position1));

        when(positionMapper.toDTO(any(Position.class))).thenReturn(positionDTO1);

        Optional<PositionDTO> foundPosition = positionService.getPositionByPositionCode("P001");

        assertThat(foundPosition).isPresent();
        assertThat(foundPosition.get().positionCode()).isEqualTo("P001");
    }

//    @Test
//    @Transactional
//    @DirtiesContext
//    void PositionService_getPositionByName_ReturnsPositionByPositionCodeDTO() {
//        when(positionRepository.findByPositionName("Manager")).thenReturn(Optional.of(position1));
//
//        when(positionMapper.toDTO(any(Position.class))).thenReturn(positionDTO1);
//
//        Optional<PositionDTO> foundPosition = positionService.getPositionByName("Manager");
//
//        assertThat(foundPosition).isPresent();
//        assertThat(foundPosition.get().positionName()).isEqualTo("Manager");
//    }
}
